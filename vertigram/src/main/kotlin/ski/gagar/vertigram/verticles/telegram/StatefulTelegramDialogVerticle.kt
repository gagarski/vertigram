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

/**
 * A skeleton that handles dialog.
 *
 * At one moment of time the verticle  has [state] to which the incoming requests (messages and callback queries) are
 * delegated. [State] can execute transitions to other [State]s for the verticle, execute side effects of the
 * transitions or do some internal state management.
 *
 * It optionally supports some opinionated ways to manage state history, cancellations and timeouts.
 *
 * This verticle can be combined with [DispatchVerticle], in that case [StatefulTelegramDialogVerticle]
 * will keep only one dialog state.
 */
abstract class StatefulTelegramDialogVerticle<Config> : TelegramDialogVerticle<Config>() {
    /**
     * Base address for [TelegramVerticle] (used with [tg])
     *
     * May be overridden by subclasses
     */
    open val telegramAddressBase = TelegramAddress.TELEGRAM_VERTICLE_BASE

    /**
     * Chat id of the dialog
     *
     * Should be overridden by subclasses
     */
    abstract val chatId: Long

    /**
     * Initial state (when the verticle is deployed)
     *
     * Should be overridden by subclasses
     */
    abstract val initialState: State

    /**
     * Global timeout of the verticle. No timeout by default. If the timeout is set, the verticle will die
     * after it is expired, meaning that the dialog state will be forgotten.
     */
    protected open val timeout: Duration? = null

    /**
     * State to use when [timeout] expires while a regular [State] is active.
     *
     * By default, a silent timeout state is used.
     */
    protected open val timeoutState: State
        get() = SilentTimeout(this)

    /**
     * State to use when [timeout] expires while an [EphemeralState] is active.
     *
     * By default, a silent timeout state is used.
     */
    protected open val ephemeralTimeoutState: EphemeralState
        get() = EphemeralSilentTimeout(this)

    /**
     * State history size
     */
    protected open val historySize: Int = 100

    /**
     * Default history behavior when [State.become] without explicit behavior is called.
     *
     * By default, the new state is pushed to the end of the history, meaning that verticle can do a rollback to it.
     */
    protected open val defaultHistoryBehavior: HistoryBehavior = HistoryBehavior.PUSH

    private val history: ArrayDeque<AbstractState> = ArrayDeque()
    @PublishedApi
    internal val mutex = Mutex()

    /**
     * Telegram client
     */
    protected val tg: Telegram by lazy {
        ThinTelegram(vertigram, telegramAddressBase)
    }

    /**
     * State
     */
    protected var state: AbstractState? = null
    private var msgInfo: MsgInfo? = null
    private var observedChatType: Chat.Type? = null
    private var timeoutTimerHandle: Job? = null

    override suspend fun start() {
        super.start()

        withLock(discardWhenBusy = false) {
            callbackQueryListenAddress?.let {
                consumer(it, function = ::handleCallbackQuery).awaitRegistration()
            }
            messageListenAddress?.let {
                consumer(it, function = ::handleMessage).awaitRegistration()
            }

            become(initialState, defaultHistoryBehavior)
        }

        scheduleTimeout()
    }

    /**
     * Execute `block` with an exclusive lock
     * @param discardWhenBusy if true, any pending `block`s will be discarded if the verticle is busy
     *      otherwise, they'll be enqueued
     * @param block block of code to execute
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

    private suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) = messageHandler {
        withLock {
            callbackQuery.message?.chat?.type?.let { observedChatType = it }
            val activeState = requireNotNull(state)
            if (activeState.ephemeralUserId?.let { it != callbackQuery.from.id } == true) {
                return@messageHandler
            }

            val ephemeralHook = EphemeralHook.from(callbackQuery)

            activeState.handleCallbackQuery(callbackQuery, ephemeralHook)
        }

    }

    private suspend fun handleMessage(message: Message) = messageHandler {
        withLock {
            val from = message.from
            observedChatType = message.chat.type
            invalidateKnownMessageIfVisible(message)
            val activeState = requireNotNull(state)
            if (activeState.ephemeralUserId?.let { it != from?.id } == true) return@messageHandler
            if (activeState is EphemeralState && chatSupportsEphemeralDelivery() && message.ephemeralMessageId == null) {
                return@messageHandler
            }

            val ephemeralMessageId = message.ephemeralMessageId
            if (ephemeralMessageId == null) {
                when (activeState) {
                    is State -> activeState.handleMessage(message)
                    is EphemeralState -> activeState.handleEphemeralMessage(message, EphemeralHook.from(message))
                }
            } else {
                activeState.handleEphemeralMessage(message, EphemeralHook.from(message))
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

        logger.lazy.debug { "$name is becoming $toState" }
        if (this.state != null) {
            handleHistory(state!!, historyBehavior)
        }

        activate(toState, ephemeralHook)

        while (true) {
            val activeState = requireNotNull(state)
            val next = when (activeState) {
                is State -> activeState.boost()
                is EphemeralState -> activeState.boost()
            } ?: break

            logger.lazy.debug { "$name is becoming $next" }
            activate(
                next,
                (activeState as? EphemeralState)?.currentEphemeralHook.takeIf { next is EphemeralState }
            )
        }
        val activeState = requireNotNull(state)
        logger.lazy.debug { "$name is applying side effect for $activeState" }
        activeState.sideEffect()
    }

    private suspend fun activate(toState: AbstractState, ephemeralHook: EphemeralHook?) {
        val from = state
        when (toState) {
            is State -> require(ephemeralHook == null) {
                "A regular state cannot be entered with an ephemeral hook"
            }
            is EphemeralState -> toState.setCurrentEphemeralHook(requireNotNull(ephemeralHook) {
                "An ephemeral state requires an ephemeral hook"
            })
        }

        resetKnownMessageIfDeliveryChanges(from, toState)
        state = toState
    }

    private suspend fun resetKnownMessageIfDeliveryChanges(from: AbstractState?, to: AbstractState) {
        val fromHook = (from as? EphemeralState)?.currentEphemeralHook
        val toHook = (to as? EphemeralState)?.currentEphemeralHook
        val changed = when {
            !chatSupportsEphemeralDelivery() -> false
            fromHook == null -> toHook != null
            toHook == null -> true
            else -> fromHook.userId != toHook.userId
        }
        if (changed) resetKnownMessage()
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
        when (val target = history.removeLast()) {
            is State -> become(target, HistoryBehavior.SKIP)
            is EphemeralState -> become(target, requireNotNull(ephemeralHook), HistoryBehavior.SKIP)
        }
    }

    private suspend fun rollbackRegular() {
        checkRollbackLock()
        if (!canRollbackRegularUnlocked()) return
        while (history.last() !is State) history.removeLast()
        become(history.removeLast() as State, HistoryBehavior.SKIP)
    }

    /**
     * Send or edit the message.
     *
     * By default, the last message sent by [sendOrEdit] will be edited instead of sending a new message while the
     * interaction remains undisrupted. A public incoming message disrupts both regular and ephemeral delivery. An
     * ephemeral incoming message disrupts regular delivery and ephemeral delivery to the same user. Callback queries
     * do not disrupt delivery because they add no visible message to the chat.
     *
     * A new message is also sent when switching between regular and ephemeral delivery, changing the ephemeral
     * receiver, or when the requested reply markup cannot be established by editing. Messages sent directly through
     * [tg] are outside this tracking.
     *
     * Must be called while holding the dialog lock.
     *
     * @param text Text of the message
     * @param buttons Reply markup of the message
     * @param forceSend Ignore the fact that the message is the last in the chat and do sending instead of editing
     */
    protected suspend fun sendOrEdit(
        text: FormattedText,
        buttons: ReplyMarkup? = null,
        forceSend: Boolean = false,
        ephemeralHook: EphemeralHook? = null
    ) {
        checkLockOwned("calling sendOrEdit without obtaining lock is not supported")
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

    private fun chatSupportsEphemeralDelivery() = observedChatType?.group != false

    private fun currentDelivery(ephemeralHook: EphemeralHook?): Delivery =
        ephemeralHook?.takeIf { chatSupportsEphemeralDelivery() }?.let(Delivery::Ephemeral) ?: Delivery.Regular

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
     * Extend this class to define a current dialog state.
     *
     * The state can be either immutable (in case of state change, [become] will be called and new state will be produced),
     * or have some internally-managed "microstate". This "microstate" is not a subject of [history] management of
     * the verticle. You can choose appropriate approach based on you needs in your case and combine both approaches.
     *
     * This class has methods delegating to the most of the [StatefulTelegramDialogVerticle] methods, meaning,
     * you probably should not call verticle methods directly.
     */
    sealed class AbstractState(
        /** The verticle. */
        @PublishedApi internal val v: StatefulTelegramDialogVerticle<*>
    ) {
        internal open val ephemeralUserId: Long? = null
        /**
         * Execute [block] with per-dialog lock
         */
        protected suspend inline fun withLock(block: () -> Unit) {
            v.withLock(block = block)
        }

        /**
         * Route a callback query through [shouldHandleCallbackQuery] to [doHandleCallbackQuery].
         *
         * [ephemeralHook] belongs to [callbackQuery] and can be carried into the next state.
         */
        internal open suspend fun handleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            if (shouldHandleCallbackQuery(callbackQuery, ephemeralHook)) {
                doHandleCallbackQuery(callbackQuery, ephemeralHook)
            }
        }

        /**
         * Handle an ephemeral message. By default, no-op handler.
         *
         * [ephemeralHook] belongs to [message] and can be carried into the next state.
         */
        internal open suspend fun handleEphemeralMessage(message: Message, ephemeralHook: EphemeralHook) {
            if (shouldHandleEphemeralMessage(message, ephemeralHook)) {
                doHandleEphemeralMessage(message, ephemeralHook)
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

        open suspend fun doHandleEphemeralMessage(message: Message, ephemeralHook: EphemeralHook) {}

        open suspend fun doHandleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {}

        /**
         * Handle child death of [StatefulTelegramDialogVerticle]. By default, die.
         *
         * Called while holding the dialog lock, so state transition and messaging APIs can be used safely.
         *
         * Can be overridden by subclasses.
         */
        open suspend fun onChildDeath(deathNotice: DeathNotice) {
            v.die(deathNotice.reason)
        }

        /**
         * A side effect which is executed when entering the state.
         *
         * This is a place to do [sendOrEdit].
         *
         * For example if asking for a name is a part of your dialog logic, here is the place to send
         * "what is your name?" message.
         */
        open suspend fun sideEffect() {}

        /**
         * A place to decide whether you want to skip this state (e.g., if you've already obtained
         * the information you're going to ask in previous dialog steps).
         *
         * If you're going to skip this state, you should return non-null new state from this function.
         * In case of skipping, no side effect of the state will be executed and the state won't be recorded
         * to the history. The boosts can be chained, i.e. next state can be skipped as well. The side and history effects
         * are applied only for first non-boosted-through state.
         */
        /**
         * Call it to turn [v] into [newState]
         */
        protected suspend fun become(
            /**
             * New state
             */
            newState: State,
            /**
             * History behavior, defaults to verticle-wide default behavior.
             *
             * This is the behavior for the **current** state, not for the new one.
             */
            historyBehavior: HistoryBehavior = v.defaultHistoryBehavior
        ) {
            v.become(newState, historyBehavior)
        }

        /**
         * Call it to turn [v] into an ephemeral [newState].
         */
        protected suspend fun become(
            newState: EphemeralState,
            ephemeralHook: EphemeralHook,
            historyBehavior: HistoryBehavior = v.defaultHistoryBehavior
        ) {
            v.become(newState, ephemeralHook, historyBehavior)
        }

        /**
         * Can state be rolled back to the immediately previous history entry?
         *
         * A previous [EphemeralState] requires a non-null [ephemeralHook].
         * Must be called while holding the dialog lock.
         */
        protected suspend fun canRollback(ephemeralHook: EphemeralHook? = null) = v.canRollback(ephemeralHook)

        /** Can state be rolled back to the latest regular state? Requires the dialog lock. */
        protected suspend fun canRollbackRegular() = v.canRollbackRegular()

        /**
         * Roll back to the immediately previous history entry.
         *
         * If it is ephemeral and [ephemeralHook] is null, history remains unchanged.
         * Must be called while holding the dialog lock.
         */
        protected suspend fun rollback(ephemeralHook: EphemeralHook? = null) {
            v.rollback(ephemeralHook)
        }

        /** Roll back to the latest regular state, skipping ephemeral history entries. Requires the dialog lock. */
        protected suspend fun rollbackRegular() {
            v.rollbackRegular()
        }

        /**
         * End the dialog and undeploy verticle.
         *
         * @see HierarchyVerticle.die
         */
        protected fun die(
            /**
             * Reason of death which will be known by children and parents.
             */
            reason: DeathReason
        ) {
            v.die(reason)
        }

        /**
         * End the dialog as completed
         */
        protected fun complete() {
            v.complete()
        }

        /**
         * End the dialog as failed
         */
        protected fun fail() {
            v.fail()
        }

        /**
         * End the dialog as cancelled
         */
        protected fun cancel() {
            v.cancel()
        }

        /** End the dialog as timed out. */
        protected fun timeout() {
            v.timeout()
        }

        /**
         * Call [StatefulTelegramDialogVerticle.sendOrEdit]
         */
        protected abstract suspend fun sendOrEdit(
            text: FormattedText,
            replyMarkup: ReplyMarkup? = null,
            forceSend: Boolean = false
        )

        /**
         * Telegram client, same as [StatefulTelegramDialogVerticle.tg]
         */
        protected val tg: Telegram
            get() = v.tg

    }

    /** A state using regular Telegram delivery. */
    abstract class State(v: StatefulTelegramDialogVerticle<*>) : AbstractState(v) {
        internal suspend fun handleMessage(message: Message) {
            if (shouldHandleMessage(message)) doHandleMessage(message)
        }

        protected open suspend fun shouldHandleMessage(message: Message): Boolean = true

        override suspend fun shouldHandleEphemeralMessage(
            message: Message,
            ephemeralHook: EphemeralHook
        ): Boolean = true

        open suspend fun doHandleMessage(message: Message) {}

        /** Return another regular state to skip this state before its side effect. */
        open fun boost(): State? = null

        /** Execute [handler] after [duration] while holding the dialog lock. */
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

    /** A state whose [sendOrEdit] calls use the current incoming ephemeral hook. */
    abstract class EphemeralState(v: StatefulTelegramDialogVerticle<*>) : AbstractState(v) {
        private var mutableEphemeralHook: EphemeralHook? = null

        internal val currentEphemeralHook: EphemeralHook
            get() = requireNotNull(mutableEphemeralHook) { "Ephemeral state has not been entered with a hook" }

        /** The hook installed for the current state entry or incoming interaction. */
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

        internal final override suspend fun handleCallbackQuery(
            callbackQuery: Update.CallbackQuery.Payload,
            ephemeralHook: EphemeralHook
        ) {
            setCurrentEphemeralHook(ephemeralHook)
            super.handleCallbackQuery(callbackQuery, ephemeralHook)
        }

        internal final override suspend fun handleEphemeralMessage(
            message: Message,
            ephemeralHook: EphemeralHook
        ) {
            setCurrentEphemeralHook(ephemeralHook)
            super.handleEphemeralMessage(message, ephemeralHook)
        }

        /** Return another state to skip this state before its side effect. */
        open fun boost(): AbstractState? = null

        /**
         * Capture the current hook and execute [handler] later while holding the dialog lock.
         *
         * The captured hook is installed only for [handler] execution; the latest hook observed before the timer fired
         * is restored afterward.
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

    sealed interface EphemeralHook {
        val userId: Long

        data class CallbackQuery(
            val user: User,
            val callbackQueryId: String
        ) : EphemeralHook {
            override val userId: Long get() = user.id
        }

        data class Message(
            val user: User,
            val messageId: Long,
            val ephemeralMessageId: Long?
        ) : EphemeralHook {
            override val userId: Long get() = user.id
        }

        data class Standalone(override val userId: Long) : EphemeralHook

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
         * Add the item to the end of the history stack
         */
        PUSH,

        /**
         * Replace the last history item
         */
        REPLACE_LAST,

        /**
         * Ignore the state
         */
        SKIP,

        /**
         * Clean history
         */
        WIPE
    }
}

internal sealed interface KnownMessageTarget {
    val id: Long

    data class Regular(override val id: Long) : KnownMessageTarget
    data class Ephemeral(override val id: Long, val receiverUserId: Long) : KnownMessageTarget
}

internal fun KnownMessageTarget.isDisruptedBy(isEphemeralMessage: Boolean, senderId: Long?): Boolean = when (this) {
    is KnownMessageTarget.Regular -> true
    is KnownMessageTarget.Ephemeral -> !isEphemeralMessage || senderId == null || senderId == receiverUserId
}
