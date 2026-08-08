package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.awaitRegistration
import ski.gagar.vertigram.coroutines.setTimerNonCancellable
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.markup.forceReply
import ski.gagar.vertigram.telegram.methods.editEphemeralMessageReplyMarkup
import ski.gagar.vertigram.telegram.methods.editEphemeralMessageText
import ski.gagar.vertigram.telegram.methods.editMessageReplyMarkup
import ski.gagar.vertigram.telegram.methods.editMessageText
import ski.gagar.vertigram.telegram.methods.getChat
import ski.gagar.vertigram.telegram.methods.sendMessage
import ski.gagar.vertigram.telegram.types.Chat
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.create
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.toChatId
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.common.HierarchyVerticle
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason
import ski.gagar.vertigram.verticles.telegram.StatefulTelegramDialogVerticle.State
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress
import java.time.Duration
import java.time.Instant

private val EPHEMERAL_HOOK_LIFETIME: Duration = Duration.ofSeconds(15)
private val EPHEMERAL_HOOK_EXPIRATION_SKEW: Duration = Duration.ofSeconds(1)

/**
 * A skeleton that handles a dialog.
 *
 * After initial activation and while the dialog remains alive, the verticle delegates incoming requests (messages and
 * callback queries) to its current [state]. A [State] can transition the verticle to another state, define entry side
 * effects, or perform internal state management.
 *
 * It also provides state history, rollback, and an optional absolute timeout. Completion and cancellation are
 * application decisions: client states explicitly call [AbstractState.complete] or [AbstractState.cancel].
 *
 * When used with [DispatchVerticle], each deployed [StatefulTelegramDialogVerticle] instance represents one dialog key.
 */
abstract class StatefulTelegramDialogVerticle<Config> : TelegramDialogVerticle<Config>() {
    /**
     * The base address for [TelegramVerticle], used by [tg].
     *
     * Subclasses may override this property.
     */
    open val telegramAddressBase = TelegramAddress.TELEGRAM_VERTICLE_BASE

    /**
     * The chat ID of the dialog.
     *
     * Subclasses must override this property.
     */
    abstract val chatId: Long

    /**
     * The initial state activated when the verticle is deployed.
     *
     * Subclasses must override this property.
     */
    abstract val initialState: State

    /**
     * Absolute timeout measured from verticle deployment. User interaction does not reset it, and there is no timeout
     * by default. When it expires, the verticle enters [timeoutState] or [ephemeralTimeoutState], depending on the active
     * state root. The default timeout states terminate the dialog silently; a custom timeout state must terminate the
     * dialog explicitly.
     */
    protected open val timeout: Duration? = null

    /**
     * The state to use when [timeout] expires while a regular [State] is active.
     *
     * By default, a silent timeout state is used.
     */
    protected open val timeoutState: State
        get() = SilentTimeout(this)

    /**
     * The state to use when [timeout] expires while an [EphemeralState] is active.
     *
     * By default, a silent timeout state is used.
     */
    protected open val ephemeralTimeoutState: EphemeralState
        get() = EphemeralSilentTimeout(this)

    /**
     * The maximum number of states retained in history.
     */
    protected open val historySize: Int = 100

    /**
     * The default history behavior when [State.become] is called without an explicit behavior.
     *
     * By default, the current state is pushed to the end of the history before the new state is activated, allowing the
     * new state to roll back to it.
     */
    protected open val defaultHistoryBehavior: HistoryBehavior = HistoryBehavior.PUSH

    private val history: ArrayDeque<AbstractState> = ArrayDeque()
    @PublishedApi
    internal val mutex = Mutex()

    /**
     * The Telegram client.
     */
    protected val tg: Telegram by lazy {
        ThinTelegram(vertigram, telegramAddressBase)
    }

    /**
     * The current state.
     */
    protected var state: AbstractState? = null
    private var msgInfo: MsgInfo? = null
    private var chatType: Chat.Type? = null
    private var timeoutTimerHandle: Job? = null

    override suspend fun start() {
        super.start()

        withLock(discardWhenBusy = false) {
            callbackQueryListenAddress?.let {
                consumer(it, function = ::processCallbackQuery).awaitRegistration()
            }
            messageListenAddress?.let {
                consumer(it, function = ::processMessage).awaitRegistration()
            }

            become(initialState, defaultHistoryBehavior)
        }

        scheduleTimeout()
    }

    /**
     * Execute [block] while holding the dialog's exclusive lock.
     *
     * Lock ownership is tied to the current coroutine `Job`, and the lock is not reentrant. Calling this method again
     * from the owning coroutine is unsupported.
     *
     * @param discardWhenBusy If `true`, discard [block] when another coroutine owns the lock; otherwise, wait for it
     * @param block Block of code to execute
     */
    protected suspend inline fun withLock(discardWhenBusy: Boolean = true, block: () -> Unit) {
        val owner = requireNotNull(currentCoroutineContext()[Job]) {
            "Dialog lock requires a coroutine Job"
        }

        if (discardWhenBusy) {
            if (!mutex.tryLock(owner)) {
                logger.lazy.debug {
                    "Discarded, $this is busy"
                }
                return
            }
            try {
                block()
            } finally {
                mutex.unlock(owner)
            }
        } else {
            mutex.withLock(owner) {
                block()
            }
        }
    }

    private suspend fun checkLockOwned(message: String) {
        val owner = requireNotNull(currentCoroutineContext()[Job]) {
            "Dialog lock requires a coroutine Job"
        }
        check(mutex.holdsLock(owner)) { message }
    }

    private suspend fun processCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) = messageHandler {
        withLock {
            callbackQuery.message?.chat?.type?.let(::observeChatType)
            val activeState = requireNotNull(state)
            if (activeState.ephemeralUserId?.let { it != callbackQuery.from.id } == true) {
                return@messageHandler
            }

            val ephemeralHook = EphemeralHook.from(callbackQuery)

            activeState.processCallbackQuery(callbackQuery, ephemeralHook)
        }

    }

    internal suspend fun processMessage(message: Message) = messageHandler {
        withLock {
            observeChatType(message.chat.type)
            invalidateKnownMessageIfVisible(message)
            val activeState = requireNotNull(state)

            when {
                activeState.ephemeralUserId?.let { it != message.from?.id } == true ->
                    return@messageHandler
                message.ephemeralMessageId != null ->
                    activeState.processEphemeralMessage(message, EphemeralHook.from(message))
                activeState is State ->
                    activeState.processMessage(message)
                activeState is EphemeralState ->
                    return@messageHandler
            }
        }
    }

    override suspend fun onChildDeath(deathNotice: DeathNotice) {
        withLock(discardWhenBusy = false) {
            if (isDead) return@withLock
            requireNotNull(state).onChildDeath(deathNotice)
        }
    }


    private fun handleHistory(state: AbstractState, historyBehavior: HistoryBehavior) {
        if (0 == historySize) {
            return
        }
        when (historyBehavior) {
            HistoryBehavior.PUSH -> history.addLast(state)
            HistoryBehavior.REPLACE_LAST -> {
                history.removeLastOrNull()
                history.addLast(state)
            }
            HistoryBehavior.WIPE -> history.clear()
            HistoryBehavior.SKIP -> {}
        }

        while (history.size > historySize) {
            history.removeFirst()
        }
    }


    private suspend fun become(
        toState: State,
        historyBehavior: HistoryBehavior = defaultHistoryBehavior
    ) = become(toState, ephemeralHook = null, historyBehavior)

    private suspend fun become(
        toState: EphemeralState,
        ephemeralHook: EphemeralHook,
        historyBehavior: HistoryBehavior = defaultHistoryBehavior
    ) = become(toState, ephemeralHook as EphemeralHook?, historyBehavior)

    private suspend fun become(
        toState: AbstractState,
        ephemeralHook: EphemeralHook?,
        historyBehavior: HistoryBehavior
    ) {
        checkLockOwned("calling become without obtaining lock is not supported")
        require(state !== toState) {
            "[$state -> $toState] Transition to same (by identity) state is not supported"
        }
        val activation = prepareActivation(toState, ephemeralHook)

        commitBecome(activation, historyBehavior)
    }

    private suspend fun prepareActivation(
        toState: AbstractState,
        ephemeralHook: EphemeralHook?
    ): PreparedActivation {
        var candidate = toState
        var candidateHook = ephemeralHook

        while (true) {
            validateActivation(candidate, candidateHook)
            val next = when (candidate) {
                is State -> candidate.boost()
                is EphemeralState -> candidate.boost()
            } ?: return PreparedActivation(candidate, candidateHook)

            candidateHook = candidateHook.takeIf { candidate is EphemeralState && next is EphemeralState }
            candidate = next
        }
    }

    private suspend fun commitBecome(
        activation: PreparedActivation,
        historyBehavior: HistoryBehavior
    ) {
        logger.lazy.debug { "$name is becoming ${activation.state}" }
        if (this.state != null) {
            handleHistory(state!!, historyBehavior)
        }

        activateValidated(activation.state, activation.ephemeralHook)
        val activeState = requireNotNull(state)
        logger.lazy.debug { "$name is applying side effect for $activeState" }
        activeState.sideEffect()
    }

    private suspend fun validateActivation(toState: AbstractState, ephemeralHook: EphemeralHook?) {
        when (toState) {
            is State -> require(ephemeralHook == null) {
                "A regular state cannot be entered with an ephemeral hook"
            }
            is EphemeralState -> {
                requireNotNull(ephemeralHook) { "An ephemeral state requires an ephemeral hook" }
                requireEphemeralCompatibleChat()
            }
        }
    }

    private suspend fun activateValidated(toState: AbstractState, ephemeralHook: EphemeralHook?) {
        val from = state
        if (toState is EphemeralState) {
            toState.setCurrentEphemeralHook(requireNotNull(ephemeralHook))
        }

        resetKnownMessageIfDeliveryChanges(from, toState)
        state = toState
    }

    private suspend fun resetKnownMessageIfDeliveryChanges(from: AbstractState?, to: AbstractState) {
        val fromHook = (from as? EphemeralState)?.currentEphemeralHook
        val toHook = (to as? EphemeralState)?.currentEphemeralHook
        val potentiallyChanged = when {
            fromHook == null -> toHook != null
            toHook == null -> true
            else -> fromHook.userId != toHook.userId
        }
        if (potentiallyChanged) resetKnownMessage()
    }

    private fun setTimerWithLock(duration: Duration, handler: suspend () -> Unit): Job =
        setTimerNonCancellable(duration) {
            messageHandler {
                withLock(discardWhenBusy = false) {
                    if (isDead) return@withLock
                    handler()
                }
            }
        }


    private suspend fun checkRollbackLock() =
        checkLockOwned("calling rollback API without obtaining lock is not supported")

    private fun canRollbackUnlocked(ephemeralHook: EphemeralHook? = null): Boolean =
        history.lastOrNull()?.let { it is State || ephemeralHook != null } == true

    private suspend fun canRollback(ephemeralHook: EphemeralHook? = null): Boolean {
        checkRollbackLock()
        return canRollbackUnlocked(ephemeralHook)
    }

    private fun canRollbackRegularUnlocked(): Boolean = history.any { it is State }

    private suspend fun canRollbackRegular(): Boolean {
        checkRollbackLock()
        return canRollbackRegularUnlocked()
    }

    private suspend fun rollback(ephemeralHook: EphemeralHook? = null) {
        checkRollbackLock()
        if (!canRollbackUnlocked(ephemeralHook)) return
        val target = history.last()
        val activation = prepareActivation(target, ephemeralHook.takeIf { target is EphemeralState })
        history.removeLast()
        commitBecome(activation, HistoryBehavior.SKIP)
    }

    private suspend fun rollbackRegular() {
        checkRollbackLock()
        if (!canRollbackRegularUnlocked()) return
        val target = history.last { it is State } as State
        val activation = prepareActivation(target, null)
        while (history.last() !== target) history.removeLast()
        history.removeLast()
        commitBecome(activation, HistoryBehavior.SKIP)
    }

    /**
     * Send or edit the message.
     *
     * By default, [sendOrEdit] edits its last message rather than sending a new one while the interaction remains
     * undisrupted. An incoming public message disrupts both regular and ephemeral delivery. An incoming ephemeral
     * message disrupts regular delivery and ephemeral delivery to the same user. Callback queries
     * do not disrupt delivery because they add no visible message to the chat.
     *
     * A new message is also sent when delivery switches between regular and ephemeral, the ephemeral receiver changes,
     * or the requested reply markup cannot be established by editing. Messages sent directly through
     * [tg] are outside this tracking.
     *
     * This method must be called while holding the dialog lock.
     *
     * @param text Text of the message
     * @param buttons Reply markup of the message
     * @param forceSend Send a new message even if the existing message could be edited
     * @param ephemeralHook Hook for ephemeral delivery. An expired interaction-derived hook causes this call to log a
     * warning and return without delivery; standalone hooks created with [EphemeralHook.forUser] do not expire.
     */
    protected suspend fun sendOrEdit(
        text: FormattedText,
        buttons: ReplyMarkup? = null,
        forceSend: Boolean = false,
        ephemeralHook: EphemeralHook? = null
    ) {
        checkLockOwned("calling sendOrEdit without obtaining lock is not supported")
        if (ephemeralHook?.isExpired() == true) {
            logger.lazy.warn(
                throwable = IllegalStateException("Unusable ephemeral hook passed to sendOrEdit")
            ) {
                "Ignoring sendOrEdit because its ephemeral hook expires at ${ephemeralHook.expiresAt} " +
                    "and is within the ${EPHEMERAL_HOOK_EXPIRATION_SKEW.seconds}-second safety margin"
            }
            return
        }
        val delivery = currentDelivery(ephemeralHook)
        val replyMarkup = buttons ?: if (delivery is Delivery.Ephemeral) forceReply(selective = false) else null

        if (forceSend) resetKnownMessage()
        if (msgInfo?.let {
                !delivery.canEdit(it.target) ||
                    it.markup != replyMarkup && replyMarkup !is ReplyMarkup.InlineKeyboard
            } == true
        ) {
            resetKnownMessage()
        }

        val knownMessage = msgInfo
        if (knownMessage == null) {
            val target = sendMessage(delivery, text, replyMarkup)
            msgInfo = MsgInfo(target, text.toString(), replyMarkup)
        } else if (text.toString() != knownMessage.text || replyMarkup != knownMessage.markup) {
            val target = editMessageText(knownMessage.target, text, replyMarkup)
            msgInfo = MsgInfo(target, text.toString(), replyMarkup)
        }
    }

    private suspend fun invalidateKnownMessageIfVisible(message: Message) {
        val target = msgInfo?.target ?: return
        if (target.isDisruptedBy(message.ephemeralMessageId != null, message.from?.id)) resetKnownMessage()
    }

    internal fun observeChatType(type: Chat.Type) {
        if (chatType == null) chatType = type
    }

    internal open suspend fun fetchChatType(): Chat.Type = tg.getChat(
        chatId = chatId.toChatId()
    ).type

    private suspend fun resolveChatType(): Chat.Type = chatType ?: fetchChatType().also(::observeChatType)

    private suspend fun requireEphemeralCompatibleChat(): Chat.Type = resolveChatType().also {
        check(it.group) {
            "Ephemeral states and delivery are supported only in group and supergroup chats"
        }
    }

    private suspend fun currentDelivery(ephemeralHook: EphemeralHook?): Delivery {
        ephemeralHook ?: return Delivery.Regular
        requireEphemeralCompatibleChat()
        return Delivery.Ephemeral(ephemeralHook)
    }

    private fun Delivery.canEdit(target: KnownMessageTarget): Boolean = when {
        this is Delivery.Regular && target is KnownMessageTarget.Regular -> true
        this is Delivery.Ephemeral && target is KnownMessageTarget.Ephemeral -> receiverUserId == target.receiverUserId
        else -> false
    }

    private suspend fun sendMessage(
        delivery: Delivery,
        text: FormattedText,
        replyMarkup: ReplyMarkup?
    ): KnownMessageTarget =
        when (delivery) {
            Delivery.Regular -> KnownMessageTarget.Regular(
                tg.sendMessage(
                    chatId = chatId.toChatId(),
                    text = text,
                    replyMarkup = replyMarkup
                ).messageId
            )
            is Delivery.Ephemeral -> {
                val message = tg.sendMessage(
                    chatId = chatId.toChatId(),
                    text = text,
                    receiverUserId = delivery.hook.userId,
                    callbackQueryId = (delivery.hook as? EphemeralHook.CallbackQuery)?.callbackQueryId,
                    replyParameters = (delivery.hook as? EphemeralHook.Message)?.let {
                        ReplyParameters.create(ephemeralMessageId = requireNotNull(it.ephemeralMessageId) {
                            "Grouped ephemeral delivery requires an ephemeral message identifier"
                        })
                    },
                    replyMarkup = replyMarkup
                )
                KnownMessageTarget.Ephemeral(
                    requireNotNull(message.ephemeralMessageId) {
                        "Telegram didn't return an ephemeral message identifier"
                    },
                    delivery.hook.userId
                )
            }
        }

    private suspend fun editMessageText(
        target: KnownMessageTarget,
        text: FormattedText,
        replyMarkup: ReplyMarkup?
    ): KnownMessageTarget = when (target) {
        is KnownMessageTarget.Regular -> KnownMessageTarget.Regular(
            tg.editMessageText(
                chatId = chatId.toChatId(),
                messageId = target.id,
                text = text,
                replyMarkup = replyMarkup as? ReplyMarkup.InlineKeyboard
            ).messageId
        )
        is KnownMessageTarget.Ephemeral -> {
            tg.editEphemeralMessageText(
                chatId = chatId.toChatId(),
                receiverUserId = target.receiverUserId,
                ephemeralMessageId = target.id,
                text = text,
                replyMarkup = replyMarkup as? ReplyMarkup.InlineKeyboard
            )
            target
        }
    }

    private suspend fun resetKnownMessage() {
        val knownMessage = msgInfo ?: return
        msgInfo = null
        if (knownMessage.markup == null) return

        when (val target = knownMessage.target) {
            is KnownMessageTarget.Regular -> tg.editMessageReplyMarkup(
                chatId = chatId.toChatId(),
                messageId = target.id,
                replyMarkup = null
            )
            is KnownMessageTarget.Ephemeral -> tg.editEphemeralMessageReplyMarkup(
                chatId = chatId.toChatId(),
                receiverUserId = target.receiverUserId,
                ephemeralMessageId = target.id,
                replyMarkup = null
            )
        }
    }

    private fun scheduleTimeout() {
        timeout?.let {
            timeoutTimerHandle = setTimerWithLock(it) {
                when (val activeState = requireNotNull(state)) {
                    is State -> become(timeoutState, HistoryBehavior.WIPE)
                    is EphemeralState -> become(
                        ephemeralTimeoutState,
                        activeState.currentEphemeralHook,
                        HistoryBehavior.WIPE
                    )
                }
            }
        }
    }

    private fun unscheduleTimeout() {
        timeoutTimerHandle?.cancel()
    }

    override suspend fun stop() {
        try {
            unscheduleTimeout()
        } finally {
            super.stop()
        }
    }

    /**
     * Extend this class to define a dialog state.
     *
     * A state can be immutable, with transitions using [become] to create a new state, or it can maintain an internal
     * "microstate." This microstate is not subject to the verticle's [history] management. Choose the approach
     * appropriate for the use case, or combine both approaches.
     *
     * This class provides methods that delegate to most of the [StatefulTelegramDialogVerticle] operations.
     */
    sealed class AbstractState(
        /** The verticle. */
        @PublishedApi internal val v: StatefulTelegramDialogVerticle<*>
    ) {
        internal open val ephemeralUserId: Long? = null
        /**
         * Execute [block] while holding the per-dialog lock.
         */
        protected suspend inline fun withLock(block: () -> Unit) {
            v.withLock(block = block)
        }

        /**
         * Process a callback query through [shouldHandleCallbackQuery] and [handleCallbackQuery].
         *
         * [ephemeralHook] belongs to [callbackQuery] and can be carried into the next state.
         */
        internal open suspend fun processCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            if (shouldHandleCallbackQuery(callbackQuery, ephemeralHook)) {
                handleCallbackQuery(callbackQuery, ephemeralHook)
            }
        }

        /**
         * Process an ephemeral message through [shouldHandleEphemeralMessage] and [handleEphemeralMessage].
         *
         * [ephemeralHook] belongs to [message] and can be carried into the next state.
         */
        internal open suspend fun processEphemeralMessage(message: Message, ephemeralHook: EphemeralHook) {
            if (shouldHandleEphemeralMessage(message, ephemeralHook)) {
                handleEphemeralMessage(message, ephemeralHook)
            }
        }

        protected open suspend fun shouldHandleEphemeralMessage(
            message: Message,
            ephemeralHook: EphemeralHook
        ): Boolean = true

        protected open suspend fun shouldHandleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ): Boolean = true

        /** Handle an ephemeral message accepted by [shouldHandleEphemeralMessage]. */
        protected open suspend fun handleEphemeralMessage(message: Message, ephemeralHook: EphemeralHook) {}

        /** Handle a callback query accepted by [shouldHandleCallbackQuery]. */
        protected open suspend fun handleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {}

        /**
         * Handle the death of a child verticle. By default, terminate the dialog.
         *
         * This method is called while holding the dialog lock, so state-transition and messaging APIs can be used safely.
         *
         * Subclasses may override this method.
         */
        open suspend fun onChildDeath(deathNotice: DeathNotice) {
            v.die(deathNotice.reason)
        }

        /**
         * A side effect that is executed when entering the state.
         *
         * This is the place to call [sendOrEdit].
         *
         * For example, if asking for a name is part of the dialog logic, this is the place to send a
         * "What is your name?" message.
         */
        open suspend fun sideEffect() {}

        /** Transition [v] to [newState]. */
        protected suspend fun become(
            /**
             * The new state.
             */
            newState: State,
            /**
             * History behavior, which defaults to the verticle-wide default.
             *
             * This is the behavior for the **current** state, not for the new one.
             */
            historyBehavior: HistoryBehavior = v.defaultHistoryBehavior
        ) {
            v.become(newState, historyBehavior)
        }

        /**
         * Transition [v] to an ephemeral [newState].
         */
        protected suspend fun become(
            newState: EphemeralState,
            ephemeralHook: EphemeralHook,
            historyBehavior: HistoryBehavior = v.defaultHistoryBehavior
        ) {
            v.become(newState, ephemeralHook, historyBehavior)
        }

        /**
         * Can the state be rolled back to the immediately previous history entry?
         *
         * Rolling back to an [EphemeralState] requires a non-null [ephemeralHook].
         * This method must be called while holding the dialog lock.
         */
        protected suspend fun canRollback(ephemeralHook: EphemeralHook? = null) = v.canRollback(ephemeralHook)

        /** Can the state be rolled back to the latest regular state? This method requires the dialog lock. */
        protected suspend fun canRollbackRegular() = v.canRollbackRegular()

        /**
         * Roll back to the immediately previous history entry.
         *
         * If the previous entry is ephemeral and [ephemeralHook] is null, history remains unchanged.
         * This method must be called while holding the dialog lock.
         */
        protected suspend fun rollback(ephemeralHook: EphemeralHook? = null) {
            v.rollback(ephemeralHook)
        }

        /** Roll back to the latest regular state, skipping ephemeral entries. This method requires the dialog lock. */
        protected suspend fun rollbackRegular() {
            v.rollbackRegular()
        }

        /**
         * End the dialog and undeploy the verticle.
         *
         * @see HierarchyVerticle.die
         */
        protected fun die(
            /**
             * Reason for death that will be reported to children and parents.
             */
            reason: DeathReason
        ) {
            v.die(reason)
        }

        /**
         * End the dialog as completed.
         */
        protected fun complete() {
            v.complete()
        }

        /**
         * End the dialog as failed.
         */
        protected fun fail() {
            v.fail()
        }

        /**
         * End the dialog as cancelled.
         */
        protected fun cancel() {
            v.cancel()
        }

        /** End the dialog as timed out. */
        protected fun timeout() {
            v.timeout()
        }

        /**
         * Call [StatefulTelegramDialogVerticle.sendOrEdit].
         */
        protected abstract suspend fun sendOrEdit(
            text: FormattedText,
            replyMarkup: ReplyMarkup? = null,
            forceSend: Boolean = false
        )

        /**
         * The Telegram client exposed by [StatefulTelegramDialogVerticle.tg].
         */
        protected val tg: Telegram
            get() = v.tg

    }

    /** A state using regular Telegram delivery. */
    abstract class State(v: StatefulTelegramDialogVerticle<*>) : AbstractState(v) {
        internal suspend fun processMessage(message: Message) {
            if (shouldHandleMessage(message)) handleMessage(message)
        }

        protected open suspend fun shouldHandleMessage(message: Message): Boolean = true

        override suspend fun shouldHandleEphemeralMessage(
            message: Message,
            ephemeralHook: EphemeralHook
        ): Boolean = true

        /** Handle a regular message accepted by [shouldHandleMessage]. */
        protected open suspend fun handleMessage(message: Message) {}

        /**
         * Return another regular state to skip activation of this state and execution of its side effect.
         *
         * This is a pure routing step. The candidate state is not active and must not invoke activation-dependent
         * operations such as [become] or [sendOrEdit].
         */
        open fun boost(): State? = null

        /**
         * Schedule [handler] to run no earlier than [duration] later.
         *
         * The handler waits for the dialog lock and runs only if the dialog is still alive.
         */
        protected fun setTimer(duration: Duration, handler: suspend () -> Unit): Job =
            v.setTimerWithLock(duration, handler)

        final override suspend fun sendOrEdit(
            text: FormattedText,
            replyMarkup: ReplyMarkup?,
            forceSend: Boolean
        ) {
            v.sendOrEdit(text, replyMarkup, forceSend)
        }
    }

    /**
     * A state whose [sendOrEdit] calls use the currently installed ephemeral hook.
     *
     * Ephemeral states and delivery are supported only in groups and supergroups.
     */
    abstract class EphemeralState(v: StatefulTelegramDialogVerticle<*>) : AbstractState(v) {
        private var mutableEphemeralHook: EphemeralHook? = null

        internal val currentEphemeralHook: EphemeralHook
            get() = requireNotNull(mutableEphemeralHook) { "Ephemeral state has not been entered with a hook" }

        /** The hook installed for the current state entry or the latest incoming interaction. */
        protected val ephemeralHook: EphemeralHook
            get() = currentEphemeralHook

        internal override val ephemeralUserId: Long?
            get() = currentEphemeralHook.userId

        internal fun setCurrentEphemeralHook(ephemeralHook: EphemeralHook) {
            this.mutableEphemeralHook = ephemeralHook
        }

        internal suspend fun <T> withCurrentEphemeralHook(
            ephemeralHook: EphemeralHook,
            handler: suspend () -> T
        ): T {
            val previousHook = mutableEphemeralHook
            setCurrentEphemeralHook(ephemeralHook)
            return try {
                handler()
            } finally {
                mutableEphemeralHook = previousHook
            }
        }

        internal final override suspend fun processCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            setCurrentEphemeralHook(ephemeralHook)
            super.processCallbackQuery(callbackQuery, ephemeralHook)
        }

        internal final override suspend fun processEphemeralMessage(
            message: Message,
            ephemeralHook: EphemeralHook
        ) {
            setCurrentEphemeralHook(ephemeralHook)
            super.processEphemeralMessage(message, ephemeralHook)
        }

        /**
         * Return another state to skip activation of this state and execution of its side effect.
         *
         * This is a pure routing step. The candidate state is not active, so [ephemeralHook] is unavailable and
         * activation-dependent operations such as [become] and [sendOrEdit] must not be invoked.
         */
        open fun boost(): AbstractState? = null

        /**
         * Capture the current hook and schedule [handler] to run no earlier than [duration] later.
         *
         * The handler waits for the dialog lock and runs only if the dialog is still alive. The captured hook is
         * installed only for [handler] execution; the latest hook observed before the timer fired is restored afterward.
         * Capturing a hook does not extend its lifetime. When an interaction-derived hook enters the final second of its
         * 15-second validity window, ephemeral delivery is suppressed and a warning is logged.
         */
        protected fun setTimer(
            duration: Duration,
            handler: suspend (EphemeralHook) -> Unit
        ): Job {
            val capturedHook = currentEphemeralHook
            return v.setTimerWithLock(duration) {
                withCurrentEphemeralHook(capturedHook) {
                    handler(capturedHook)
                }
            }
        }

        final override suspend fun sendOrEdit(
            text: FormattedText,
            replyMarkup: ReplyMarkup?,
            forceSend: Boolean
        ) {
            v.sendOrEdit(text, replyMarkup, forceSend, currentEphemeralHook)
        }
    }

    private class SilentTimeout(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : State(verticle) {
        override suspend fun sideEffect() = verticle.timeout()
    }

    private class EphemeralSilentTimeout(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : EphemeralState(verticle) {
        override suspend fun sideEffect() = verticle.timeout()
    }

    private data class MsgInfo(val target: KnownMessageTarget, val text: String, val markup: ReplyMarkup?)

    private sealed interface Delivery {
        data object Regular : Delivery
        data class Ephemeral(val hook: EphemeralHook) : Delivery {
            val receiverUserId: Long get() = hook.userId
        }
    }

    private data class PreparedActivation(
        val state: AbstractState,
        val ephemeralHook: EphemeralHook?
    )

    /**
     * A context that permits ephemeral delivery to [userId].
     *
     * Hooks created from incoming interactions expire after 15 seconds. [forUser] creates a non-expiring hook for
     * administrator-initiated delivery.
     */
    sealed interface EphemeralHook {
        val userId: Long
        val expiresAt: Instant?

        data class CallbackQuery(
            val user: User,
            val callbackQueryId: String,
            override val expiresAt: Instant = Instant.now().plus(EPHEMERAL_HOOK_LIFETIME)
        ) : EphemeralHook {
            override val userId: Long get() = user.id
        }

        data class Message(
            val user: User,
            val messageId: Long,
            val ephemeralMessageId: Long?,
            override val expiresAt: Instant = Instant.now().plus(EPHEMERAL_HOOK_LIFETIME)
        ) : EphemeralHook {
            override val userId: Long get() = user.id
        }

        data class Standalone(override val userId: Long) : EphemeralHook {
            override val expiresAt: Instant? = null
        }

        companion object {
            fun forUser(userId: Long): EphemeralHook = Standalone(userId)

            fun from(callbackQuery: Update.CallbackQuery.Payload): EphemeralHook =
                CallbackQuery(callbackQuery.from, callbackQuery.id)

            fun from(message: ski.gagar.vertigram.telegram.types.Message): EphemeralHook = Message(
                requireNotNull(message.from) { "An ephemeral hook requires a message sender" },
                message.messageId,
                message.ephemeralMessageId
            )
        }
    }

    /**
     * History behavior
     *
     * @see defaultHistoryBehavior
     * @see State.become
     */
    enum class HistoryBehavior {
        /**
         * Add the state being left to the end of the history stack.
         */
        PUSH,

        /**
         * Replace the last history item with the state being left.
         */
        REPLACE_LAST,

        /**
         * Do not record the state being left.
         */
        SKIP,

        /**
         * Clear the history without recording the state being left.
         */
        WIPE
    }
}

internal fun StatefulTelegramDialogVerticle.EphemeralHook.isExpired(now: Instant = Instant.now()): Boolean =
    expiresAt?.minus(EPHEMERAL_HOOK_EXPIRATION_SKEW)?.let { !now.isBefore(it) } == true

internal sealed interface KnownMessageTarget {
    val id: Long

    data class Regular(override val id: Long) : KnownMessageTarget
    data class Ephemeral(override val id: Long, val receiverUserId: Long) : KnownMessageTarget
}

internal fun KnownMessageTarget.isDisruptedBy(isEphemeralMessage: Boolean, senderId: Long?): Boolean = when (this) {
    is KnownMessageTarget.Regular -> true
    is KnownMessageTarget.Ephemeral -> !isEphemeralMessage || senderId == null || senderId == receiverUserId
}
