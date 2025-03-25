package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.coroutines.setTimerNonCancellable
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.markup.toRichText
import ski.gagar.vertigram.telegram.methods.editMessageReplyMarkup
import ski.gagar.vertigram.telegram.methods.editMessageText
import ski.gagar.vertigram.telegram.methods.sendMessage
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.isCommandForBot
import ski.gagar.vertigram.telegram.types.util.toChatId
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.common.AbstractHierarchyVerticle
import ski.gagar.vertigram.verticles.common.address.VertigramCommonAddress
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
 * This verticle can be combined with [AbstractDispatchVerticle], in that case [StatefulTelegramDialogVerticle]
 * will keep only one dialog state.
 */
abstract class StatefulTelegramDialogVerticle<Config> : AbstractHierarchyVerticle<Config>() {
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
     * Callback query listen address. By default — a private address, meaning that only parent verticle knows it.
     *
     * May be overridden by subclasses
     *
     * @see AbstractHierarchyVerticle
     */
    open val callbackQueryListenAddress: String
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, TelegramAddress.Dialog.Classifier.CallbackQuery)

    /**
     * Callback query listen address. By default — a private address, meaning that only parent verticle knows it.
     *
     * May be overridden by subclasses
     *
     * @see AbstractHierarchyVerticle
     */
    open val messageListenAddress: String
        get() = VertigramCommonAddress.Private.withClassifier(deploymentID, TelegramAddress.Dialog.Classifier.Message)

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
    private var timeoutTimerHandle: Job? = null

    override suspend fun start() {
        super.start()
        becomeWithLock(initialState, defaultHistoryBehavior)
        consumer(callbackQueryListenAddress, function = ::handleCallbackQuery)
        consumer(messageListenAddress, function = ::handleMessage)
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
            if (handleCancel && callbackQuery.data == CANCEL) {
                become(cancelState, HistoryBehavior.WIPE)
                return@messageHandler
            }

            if (handleRollback && callbackQuery.data == BACK) {
                rollback()
                return@messageHandler
            }

            state!!.handleCallbackQuery(callbackQuery)
        }

    }

    private suspend fun handleMessage(message: Message) = messageHandler {
        withLock {
            resetKnownMessage()

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
        val state = history.removeLast()
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
     */
    protected suspend fun sendOrEdit(
        richText: RichText,
        buttons: ReplyMarkup? = null,
        forceSend: Boolean = false
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

    private fun resetKnownMessage() {
        if (null != msgInfo) {
            prevMsgInfo = msgInfo
        }
        msgInfo = null
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
         * @see AbstractHierarchyVerticle.die
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
        protected suspend fun sendOrEdit(
            richText: RichText, buttons: ReplyMarkup.InlineKeyboard? = null, forceSend: Boolean = false
        ) {
            v.sendOrEdit(richText, buttons, forceSend)
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

    private class YawnTimeout(private val verticle: StatefulTelegramDialogVerticle<*>) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("\uD83E\uDD71".toRichText())
            verticle.timeout()
        }
    }

    private class SilentTimeout(private val verticle: StatefulTelegramDialogVerticle<*>) : State(verticle) {
        override suspend fun sideEffect() {
            verticle.timeout()
        }
    }

    private class CrossCancelled(private val verticle: StatefulTelegramDialogVerticle<*>) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("❌".toRichText())
            cancel()
        }
    }

    private class SilentCancelled(private val verticle: StatefulTelegramDialogVerticle<*>) : State(verticle) {
        override suspend fun sideEffect() {
            cancel()
        }
    }

    private class CheckmarkDone(private val verticle: StatefulTelegramDialogVerticle<*>) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit("✅".toRichText())
            complete()
        }
    }

    private class SilentDone(private val verticle: StatefulTelegramDialogVerticle<*>) : State(verticle) {
        override suspend fun sideEffect() {
            complete()
        }
    }

    private data class MsgInfo(val id: Long, val hasButtons: Boolean, val text: String, val markup: ReplyMarkup?)

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
