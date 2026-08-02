package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.awaitRegistration
import ski.gagar.vertigram.coroutines.setTimerNonCancellable
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.markup.toFormattedText
import ski.gagar.vertigram.telegram.markup.forceReply
import ski.gagar.vertigram.telegram.methods.answerCallbackQuery
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
import ski.gagar.vertigram.telegram.types.util.isCommandForBot
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
     * Bot identity (a response from `getMe` Telegram method).
     *
     * Should be overridden by subclasses
     */
    protected open val me: User.Me? = null

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
     * Should handle cancel message (/cancel) or cancel query callback
     */
    protected open val handleCancel: Boolean = false

    /**
     * Should handle back message (/back) or back query callback
     */
    protected open val handleRollback: Boolean = false

    /**
     * Global timeout of the verticle. No timeout by default. If the timeout is set, the verticle will die
     * after it is expired, meaning that the dialog state will be forgotten.
     */
    protected open val timeout: Duration? = null

    /**
     * State to use as cancelled state if [handleCancel] is true.
     *
     * By default, a silent cancelled state is used (without side effects such as sending messages)
     */
    protected open val cancelState: State = silentCancelled()

    /**
     * State to use as cancelled state if [timeout] is set.
     *
     * By default, a silent cancelled state is used (without side effects such as sending messages)
     */
    protected open val timeoutState: State = silentTimeout()

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

    private val history: ArrayDeque<State> = ArrayDeque()
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
    protected var state: State? = null
    private var msgInfo: MsgInfo? = null
    private var ephemeralUser: User? = null
    private var lastEphemeralContext: EphemeralContext? = null
    private var currentEphemeralContext: EphemeralContext? = null
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
        if (discardWhenBusy && mutex.isLocked) {
            logger.lazy.debug {
                "Discarded, $this is busy"
            }
            return
        }

        mutex.withLock {
            block()
        }
    }

    private suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) = messageHandler {
        withLock {
            callbackQuery.message?.chat?.type?.let { observedChatType = it }
            if (ephemeralUser?.id?.let { it != callbackQuery.from.id } == true) return@messageHandler
            val context = EphemeralContext.CallbackQuery(callbackQuery.from.id, callbackQuery.id)
            lastEphemeralContext = context
            if (ephemeralUser != null) currentEphemeralContext = context

            if (handleCancel && callbackQuery.data == CANCEL) {
                tg.answerCallbackQuery(
                    callbackQueryId = callbackQuery.id,
                )
                become(cancelState, HistoryBehavior.WIPE)
                return@messageHandler
            }

            if (handleRollback && callbackQuery.data == BACK) {
                tg.answerCallbackQuery(
                    callbackQueryId = callbackQuery.id,
                )
                rollback()
                return@messageHandler
            }

            state!!.handleCallbackQuery(callbackQuery)
        }

    }

    private suspend fun handleMessage(message: Message) = messageHandler {
        withLock {
            val from = message.from
            observedChatType = message.chat.type
            invalidateKnownMessageIfVisible(message)
            if (ephemeralUser?.id?.let { it != from?.id } == true) return@messageHandler

            val contextEphemeralMessageId = message.ephemeralMessageId
            if (usesEphemeralDelivery() && contextEphemeralMessageId == null) return@messageHandler
            lastEphemeralContext = if (contextEphemeralMessageId != null && from != null) {
                EphemeralContext.Message(from.id, contextEphemeralMessageId)
            } else {
                null
            }
            if (ephemeralUser != null) currentEphemeralContext = lastEphemeralContext

            if (handleCancel && message.isCommandForBot(CANCEL, me)) {
                become(cancelState, HistoryBehavior.WIPE)
                return@messageHandler
            }

            if (handleRollback && message.isCommandForBot(BACK, me)) {
                rollback()
                return@messageHandler
            }

            state!!.handleMessage(message)
        }
    }

    override suspend fun onChildDeath(deathNotice: DeathNotice) {
        state!!.onChildDeath(deathNotice)
    }


    private fun handleHistory(state: State, historyBehavior: HistoryBehavior) {
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
    ) {
        check(mutex.isLocked) {
            "calling become without obtaining lock is not supported"
        }
        require(state !== toState) {
            "[$state -> $toState] Transition to same (by identity) state is not supported"
        }

        logger.lazy.debug { "$name is becoming $toState" }
        if (this.state != null) {
            handleHistory(state!!, historyBehavior)
        }

        if (usesEphemeralDelivery()) {
            currentEphemeralContext = requireNotNull(lastEphemeralContext) {
                "Ephemeral mode requires a message or callback-query context before becoming another state"
            }
        }
        state = toState

        while (true) {
            val next = state!!.boost() ?: break

            logger.lazy.debug { "$name is becoming $toState" }
            state = next
        }
        logger.lazy.debug { "$name is applying side effect for $toState" }
        state!!.sideEffect()
    }

    private suspend fun becomeWithLock(
        toState: State,
        historyBehavior: HistoryBehavior = defaultHistoryBehavior
    ) {
        withLock {
            become(toState, historyBehavior)
        }
    }

    private suspend fun becomeEphemeral(user: User) {
        check(mutex.isLocked) { "calling becomeEphemeral without obtaining lock is not supported" }
        val context = lastEphemeralContext
        if (chatSupportsEphemeralDelivery()) {
            requireNotNull(context) {
                "Ephemeral mode requires a message or callback-query context"
            }
            require(context.receiverUserId == user.id) {
                "Ephemeral context belongs to a different user"
            }
        }

        if (ephemeralUser == null) {
            if (chatSupportsEphemeralDelivery()) resetKnownMessage()
        } else if (ephemeralUser?.id != user.id) {
            resetKnownMessage()
        }
        ephemeralUser = user
        currentEphemeralContext = context
    }

    private suspend fun becomeNormal() {
        check(mutex.isLocked) { "calling becomeNormal without obtaining lock is not supported" }
        if (usesEphemeralDelivery()) {
            resetKnownMessage()
        }
        ephemeralUser = null
        lastEphemeralContext = null
        currentEphemeralContext = null
    }

    private fun setTimerWithLock(duration: Duration, handler: suspend () -> Unit): Job =
        setTimerNonCancellable(duration) {
            withLock { handler() }
        }


    private fun canRollback() = history.isNotEmpty()

    private suspend fun rollback() {
        val state = history.removeLastOrNull() ?: return
        become(state, HistoryBehavior.SKIP)
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
     * @param text Text of the message
     * @param buttons Reply markup of the message
     * @param forceSend Ignore the fact that the message is the last in the chat and do sending instead of editing
     */
    protected suspend fun sendOrEdit(
        text: FormattedText,
        buttons: ReplyMarkup? = null,
        forceSend: Boolean = false
    ) {
        val delivery = currentDelivery()
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

    private fun usesEphemeralDelivery() = ephemeralUser != null && chatSupportsEphemeralDelivery()

    private fun currentDelivery(): Delivery = ephemeralUser?.takeIf { chatSupportsEphemeralDelivery() }?.let { user ->
        Delivery.Ephemeral(
            user.id,
            requireNotNull(currentEphemeralContext) { "Ephemeral mode has no delivery context" }
        )
    } ?: Delivery.Regular

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
                    receiverUserId = delivery.receiverUserId,
                    callbackQueryId = (delivery.context as? EphemeralContext.CallbackQuery)?.callbackQueryId,
                    replyParameters = (delivery.context as? EphemeralContext.Message)?.let {
                        ReplyParameters.create(ephemeralMessageId = it.ephemeralMessageId)
                    },
                    replyMarkup = replyMarkup
                )
                KnownMessageTarget.Ephemeral(
                    requireNotNull(message.ephemeralMessageId) {
                        "Telegram didn't return an ephemeral message identifier"
                    },
                    delivery.receiverUserId
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
                become(timeoutState, HistoryBehavior.WIPE)
                timeout()
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
    abstract class State(
        /**
         * The verticle
         */
        @PublishedApi internal val v: StatefulTelegramDialogVerticle<*>
    ) {
        /**
         * Execute [block] with per-dialog lock
         */
        protected suspend inline fun withLock(block: () -> Unit) {
            v.withLock(block = block)
        }

        /**
         * Handle callback query. By default, no-op handler.
         *
         * Can be overridden by subclasses.
         */
        open suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) {}

        /**
         * Handle message. By default, no-op handler.
         *
         * Can be overridden by subclasses.
         */
        open suspend fun handleMessage(message: Message) {}

        /**
         * Handle child death of [StatefulTelegramDialogVerticle]. By default, die.
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
        open fun boost(): State? = null

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

        /** Enter ephemeral delivery mode, latching [user] until [becomeNormal] is called. */
        protected suspend fun becomeEphemeral(user: User) {
            v.becomeEphemeral(user)
        }

        /** Leave ephemeral delivery mode. */
        protected suspend fun becomeNormal() {
            v.becomeNormal()
        }

        /**
         * Execute [become] [withLock].
         */
        protected suspend fun becomeWithLock(
            toState: State,
            historyBehavior: HistoryBehavior = v.defaultHistoryBehavior
        ) {
            v.becomeWithLock(toState, historyBehavior)
        }

        /**
         * Execute [handler] after [duration].
         */
        protected fun setTimerWithLock(duration: Duration, handler: suspend () -> Unit): Job =
            v.setTimerWithLock(duration, handler)

        /**
         * Can state be rolled back (use it to decide on rendering back button or when performing the rollback itself)
         */
        protected fun canRollback() = v.canRollback()

        /**
         * Perform a rollback
         */
        protected suspend fun rollback() {
            v.rollback()
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

        /**
         * Call [StatefulTelegramDialogVerticle.sendOrEdit]
         */
        protected open suspend fun sendOrEdit(
            text: FormattedText,
            replyMarkup: ReplyMarkup? = null,
            forceSend: Boolean = false
        ) {
            v.sendOrEdit(text, replyMarkup, forceSend)
        }

        /**
         * Telegram client, same as [StatefulTelegramDialogVerticle.tg]
         */
        protected val tg: Telegram
            get() = v.tg

        /**
         * Get [YawnTimeout] state (use together with [become] or [becomeWithLock]): [sendOrEdit] a yawning emoji and [die] as timed out.
         */
        protected fun yawnTimeout(): State = YawnTimeout(v)
        /**
         * Get [SilentTimeout] state (use together with [become] or [becomeWithLock]): do not send anything and [die] with timeout.
         */
        protected fun silentTimeout(): State = SilentTimeout(v)
        /**
         * Get [CrossCancelled] state (use together with [become] or [becomeWithLock]): [sendOrEdit] a redd cross sign emoji and [die] as cancelled.
         */
        protected fun crossCancelled(): State = CrossCancelled(v)
        /**
         * Get [SilentCancelled] state (use together with [become] or [becomeWithLock]): do not send anything and [die] as cancelled.
         */
        protected fun silentCancelled(): State = SilentCancelled(v)
        /**
         * Get [CheckmarkDone] state (use together with [become] or [becomeWithLock]): [sendOrEdit] green checkmark emoji and [die] as completed.
         */
        protected fun checkmarkDone(): State = CheckmarkDone(v)
        /**
         * Get [SilentDone] state (use together with [become] or [becomeWithLock]): do not send anything and [die] as completed.
         */
        protected fun silentDone(): State = SilentDone(v)

    }

    protected fun yawnTimeout(): State = YawnTimeout(this)
    protected fun silentTimeout(): State = SilentTimeout(this)
    protected fun crossCancelled(): State = CrossCancelled(this)
    protected fun silentCancelled(): State = SilentCancelled(this)
    protected fun checkmarkDone(): State = CheckmarkDone(this)
    protected fun silentDone(): State = SilentDone(this)

    private class YawnTimeout(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("\uD83E\uDD71".toFormattedText())
            verticle.timeout()
        }
    }

    private class SilentTimeout(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : State(verticle) {
        override suspend fun sideEffect() {
            verticle.timeout()
        }
    }

    private class CrossCancelled(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("❌".toFormattedText())
            cancel()
        }
    }

    private class SilentCancelled(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : State(verticle) {
        override suspend fun sideEffect() {
            cancel()
        }
    }

    private class CheckmarkDone(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("✅".toFormattedText())
            complete()
        }
    }

    private class SilentDone(
        private val verticle: StatefulTelegramDialogVerticle<*>
    ) : State(verticle) {
        override suspend fun sideEffect() {
            complete()
        }
    }

    private data class MsgInfo(val target: KnownMessageTarget, val text: String, val markup: ReplyMarkup?)

    private sealed interface Delivery {
        data object Regular : Delivery
        data class Ephemeral(val receiverUserId: Long, val context: EphemeralContext) : Delivery
    }

    /**
     * Eligible incoming action authorizing an ephemeral response.
     */
    private sealed interface EphemeralContext {
        val receiverUserId: Long

        data class CallbackQuery(
            override val receiverUserId: Long,
            val callbackQueryId: String
        ) : EphemeralContext

        data class Message(
            override val receiverUserId: Long,
            val ephemeralMessageId: Long
        ) : EphemeralContext
    }

    companion object {
        /**
         * Cancel command
         */
        const val CANCEL = "cancel"

        /**
         * Back command
         */
        const val BACK = "back"
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
