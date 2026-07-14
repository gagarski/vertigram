package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.coroutines.setTimerNonCancellable
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.markup.toRichText
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
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.richtext.RichText
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
     * Type of the dialog chat, when it is known before the first incoming update.
     *
     * Override this to allow the [initialState] to send an ephemeral message in a group or supergroup. Otherwise,
     * the chat type is learned from incoming messages and callback queries. Unknown and non-group chats use regular
     * message delivery even when [Delivery.Ephemeral] is requested.
     */
    protected open val chatType: Chat.Type? = null

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
    private var prevMsgInfo: MsgInfo? = null
    private var msgInfo: MsgInfo? = null
    private val ephemeralMsgInfo = mutableMapOf<Long, MsgInfo>()
    private var observedChatType: Chat.Type? = null
    private var timeoutTimerHandle: Job? = null

    override suspend fun start() {
        super.start()
        becomeWithLock(initialState, defaultHistoryBehavior)
        callbackQueryListenAddress?.let {
            consumer(it, function = ::handleCallbackQuery)
        }
        messageListenAddress?.let {
            consumer(it, function = ::handleMessage)
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
        }

        mutex.withLock {
            block()
        }
    }

    private suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) = messageHandler {
        withLock {
            observedChatType = callbackQuery.message?.chat?.type ?: observedChatType

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
            observedChatType = message.chat.type

            val repliedEphemeralMessageId = message.replyToMessage?.ephemeralMessageId ?: message.ephemeralMessageId
            if (repliedEphemeralMessageId == null) {
                resetKnownMessage()
            } else {
                message.from?.id?.let { resetKnownEphemeralMessage(it) }
            }

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
     * By default, the last sent message (sent by [sendOrEdit] itself) will be edited instead of sending a new message
     * if there were no messages in the chat after it has been sent (i.e. we think that it is the last message in the chat).
     * Otherwise, a new message will be sent.
     *
     * @param richText Text of the message
     * @param buttons Reply markup of the message
     * @param forceSend Ignore the fact that the message is the last in the chat and do sending instead of editing
     * @param delivery Whether to maintain a regular dialog message or an ephemeral message for a specific user
     */
    protected suspend fun sendOrEdit(
        richText: RichText,
        buttons: ReplyMarkup? = null,
        forceSend: Boolean = false,
        delivery: Delivery = Delivery.Regular
    ) {
        val effectiveDelivery = delivery.takeIf { supportsEphemeralMessages() } ?: Delivery.Regular
        when (effectiveDelivery) {
            Delivery.Regular -> sendOrEditRegular(richText, buttons, forceSend)
            is Delivery.Ephemeral -> sendOrEditEphemeral(richText, buttons, forceSend, effectiveDelivery)
        }
    }

    private fun supportsEphemeralMessages(): Boolean = when (chatType ?: observedChatType) {
        Chat.Type.GROUP, Chat.Type.SUPERGROUP -> true
        else -> false
    }

    private suspend fun sendOrEditRegular(
        richText: RichText,
        buttons: ReplyMarkup?,
        forceSend: Boolean
    ) {
        if (forceSend) {
            resetKnownMessage()
        }
        val prevMsgInfo = this.prevMsgInfo

        if (null != prevMsgInfo && prevMsgInfo.hasButtons) {
            tg.editMessageReplyMarkup(
                chatId = chatId.toChatId(),
                messageId = prevMsgInfo.id,
                replyMarkup = null
            )
            this.prevMsgInfo = null
        }

        val msgInfo = this.msgInfo

        if (null == msgInfo) {
            val msgId = tg.sendMessage(
                chatId = chatId.toChatId(),
                richText = richText,
                replyMarkup = buttons
            ).messageId
            this.msgInfo = MsgInfo(msgId, buttons != null, richText.toString(), buttons)
        } else if (richText.toString() != msgInfo.text || buttons != msgInfo.markup) {
            val msgId = tg.editMessageText(
                chatId = chatId.toChatId(),
                messageId = msgInfo.id,
                richText = richText,
                replyMarkup = buttons
            ).messageId
            this.msgInfo = MsgInfo(msgId, buttons != null, richText.toString(), buttons)
        }
    }

    private suspend fun sendOrEditEphemeral(
        richText: RichText,
        buttons: ReplyMarkup?,
        forceSend: Boolean,
        delivery: Delivery.Ephemeral
    ) {
        val replyMarkup = buttons ?: forceReply(selective = false)
        var msgInfo = ephemeralMsgInfo[delivery.receiverUserId]

        if (forceSend && msgInfo != null) {
            if (msgInfo.hasButtons) {
                tg.editEphemeralMessageReplyMarkup(
                    chatId = chatId.toChatId(),
                    receiverUserId = delivery.receiverUserId,
                    ephemeralMessageId = msgInfo.id,
                    replyMarkup = null
                )
            }
            ephemeralMsgInfo.remove(delivery.receiverUserId)
            msgInfo = null
        }

        if (msgInfo != null && replyMarkup !is ReplyMarkup.InlineKeyboard && msgInfo.markup != replyMarkup) {
            if (msgInfo.hasButtons) {
                tg.editEphemeralMessageReplyMarkup(
                    chatId = chatId.toChatId(),
                    receiverUserId = delivery.receiverUserId,
                    ephemeralMessageId = msgInfo.id,
                    replyMarkup = null
                )
            }
            ephemeralMsgInfo.remove(delivery.receiverUserId)
            msgInfo = null
        }

        if (msgInfo == null) {
            val message = tg.sendMessage(
                chatId = chatId.toChatId(),
                richText = richText,
                receiverUserId = delivery.receiverUserId,
                callbackQueryId = delivery.callbackQueryId,
                replyMarkup = replyMarkup
            )
            val ephemeralMessageId = requireNotNull(message.ephemeralMessageId) {
                "Telegram didn't return an ephemeral message identifier"
            }
            ephemeralMsgInfo[delivery.receiverUserId] =
                MsgInfo(ephemeralMessageId, true, richText.toString(), replyMarkup)
        } else if (richText.toString() != msgInfo.text || replyMarkup != msgInfo.markup) {
            tg.editEphemeralMessageText(
                chatId = chatId.toChatId(),
                receiverUserId = delivery.receiverUserId,
                ephemeralMessageId = msgInfo.id,
                richText = richText,
                replyMarkup = replyMarkup as? ReplyMarkup.InlineKeyboard
            )
            ephemeralMsgInfo[delivery.receiverUserId] =
                MsgInfo(msgInfo.id, true, richText.toString(), replyMarkup)
        }
    }

    private fun resetKnownMessage() {
        if (null != msgInfo) {
            prevMsgInfo = msgInfo
        }
        msgInfo = null
    }

    private suspend fun resetKnownEphemeralMessage(receiverUserId: Long) {
        val msgInfo = ephemeralMsgInfo.remove(receiverUserId) ?: return
        if (msgInfo.hasButtons) {
            tg.editEphemeralMessageReplyMarkup(
                chatId = chatId.toChatId(),
                receiverUserId = receiverUserId,
                ephemeralMessageId = msgInfo.id,
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

    override fun beforeDeath(reason: DeathReason) {
        unscheduleTimeout()
        super.beforeDeath(reason)
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
            richText: RichText,
            replyMarkup: ReplyMarkup? = null,
            forceSend: Boolean = false,
            delivery: Delivery = Delivery.Regular
        ) {
            v.sendOrEdit(richText, replyMarkup, forceSend, delivery)
        }

        /**
         * Telegram client, same as [StatefulTelegramDialogVerticle.tg]
         */
        protected val tg: Telegram
            get() = v.tg

        /**
         * Get [YawnTimeout] state (use together with [become] or [becomeWithLock]): [sendOrEdit] a yawning emoji and [die] as timed out.
         */
        protected fun yawnTimeout(delivery: Delivery = Delivery.Regular): State = YawnTimeout(v, delivery)
        /**
         * Get [SilentTimeout] state (use together with [become] or [becomeWithLock]): do not send anything and [die] with timeout.
         */
        protected fun silentTimeout(delivery: Delivery = Delivery.Regular): State = SilentTimeout(v, delivery)
        /**
         * Get [CrossCancelled] state (use together with [become] or [becomeWithLock]): [sendOrEdit] a redd cross sign emoji and [die] as cancelled.
         */
        protected fun crossCancelled(delivery: Delivery = Delivery.Regular): State = CrossCancelled(v, delivery)
        /**
         * Get [SilentCancelled] state (use together with [become] or [becomeWithLock]): do not send anything and [die] as cancelled.
         */
        protected fun silentCancelled(delivery: Delivery = Delivery.Regular): State = SilentCancelled(v, delivery)
        /**
         * Get [CheckmarkDone] state (use together with [become] or [becomeWithLock]): [sendOrEdit] green checkmark emoji and [die] as completed.
         */
        protected fun checkmarkDone(delivery: Delivery = Delivery.Regular): State = CheckmarkDone(v, delivery)
        /**
         * Get [SilentDone] state (use together with [become] or [becomeWithLock]): do not send anything and [die] as completed.
         */
        protected fun silentDone(delivery: Delivery = Delivery.Regular): State = SilentDone(v, delivery)

    }

    protected fun yawnTimeout(delivery: Delivery = Delivery.Regular): State = YawnTimeout(this, delivery)
    protected fun silentTimeout(delivery: Delivery = Delivery.Regular): State = SilentTimeout(this, delivery)
    protected fun crossCancelled(delivery: Delivery = Delivery.Regular): State = CrossCancelled(this, delivery)
    protected fun silentCancelled(delivery: Delivery = Delivery.Regular): State = SilentCancelled(this, delivery)
    protected fun checkmarkDone(delivery: Delivery = Delivery.Regular): State = CheckmarkDone(this, delivery)
    protected fun silentDone(delivery: Delivery = Delivery.Regular): State = SilentDone(this, delivery)

    private class YawnTimeout(
        private val verticle: StatefulTelegramDialogVerticle<*>,
        private val terminalDelivery: Delivery
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("\uD83E\uDD71".toRichText(), delivery = terminalDelivery)
            verticle.timeout()
        }
    }

    private class SilentTimeout(
        private val verticle: StatefulTelegramDialogVerticle<*>,
        @Suppress("unused") private val terminalDelivery: Delivery
    ) : State(verticle) {
        override suspend fun sideEffect() {
            verticle.timeout()
        }
    }

    private class CrossCancelled(
        private val verticle: StatefulTelegramDialogVerticle<*>,
        private val terminalDelivery: Delivery
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("❌".toRichText(), delivery = terminalDelivery)
            cancel()
        }
    }

    private class SilentCancelled(
        private val verticle: StatefulTelegramDialogVerticle<*>,
        @Suppress("unused") private val terminalDelivery: Delivery
    ) : State(verticle) {
        override suspend fun sideEffect() {
            cancel()
        }
    }

    private class CheckmarkDone(
        private val verticle: StatefulTelegramDialogVerticle<*>,
        private val terminalDelivery: Delivery
    ) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("✅".toRichText(), delivery = terminalDelivery)
            complete()
        }
    }

    private class SilentDone(
        private val verticle: StatefulTelegramDialogVerticle<*>,
        @Suppress("unused") private val terminalDelivery: Delivery
    ) : State(verticle) {
        override suspend fun sideEffect() {
            complete()
        }
    }

    private data class MsgInfo(val id: Long, val hasButtons: Boolean, val text: String, val markup: ReplyMarkup?)

    /**
     * Selects which dialog message [sendOrEdit] maintains.
     *
     * Regular and ephemeral messages are tracked independently. Ephemeral messages are tracked per receiver,
     * allowing a dialog to maintain a regular message and private messages for multiple users in parallel.
     */
    sealed interface Delivery {
        data object Regular : Delivery

        data class Ephemeral(
            /** User who can see and interact with the ephemeral message. */
            val receiverUserId: Long,
            /** Callback query that prompted a newly sent ephemeral message, if applicable. */
            val callbackQueryId: String? = null
        ) : Delivery
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
