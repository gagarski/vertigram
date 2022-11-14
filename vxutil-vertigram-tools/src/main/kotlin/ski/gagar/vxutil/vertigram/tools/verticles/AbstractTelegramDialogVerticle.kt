package ski.gagar.vxutil.vertigram.tools.verticles

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vxutil.coroutines.setTimerNonCancellable
import ski.gagar.vxutil.jackson.suspendJsonConsumer
import ski.gagar.vxutil.lazy
import ski.gagar.vxutil.logger
import ski.gagar.vxutil.verticles.address.VxUtilAddress
import ski.gagar.vxutil.verticles.children.AbstractHierarchyVerticle
import ski.gagar.vxutil.verticles.children.messages.DeathNotice
import ski.gagar.vxutil.verticles.children.messages.DeathReason
import ski.gagar.vxutil.vertigram.client.Telegram
import ski.gagar.vxutil.vertigram.client.TgVTelegram
import ski.gagar.vxutil.vertigram.editMessageReplyMarkup
import ski.gagar.vxutil.vertigram.editMessageText
import ski.gagar.vxutil.vertigram.sendMessage
import ski.gagar.vxutil.vertigram.tools.isCommandForBot
import ski.gagar.vxutil.vertigram.tools.verticles.address.VertigramAddress
import ski.gagar.vxutil.vertigram.types.CallbackQuery
import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.Me
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import ski.gagar.vxutil.vertigram.types.toChatId
import ski.gagar.vxutil.vertigram.builders.Markdown
import ski.gagar.vxutil.vertigram.builders.md
import ski.gagar.vxutil.vertigram.verticles.TelegramVerticle
import java.time.Duration

abstract class AbstractTelegramDialogVerticle : AbstractHierarchyVerticle() {
    protected open val me: Me? = null
    open val tgVAddressBase = TelegramVerticle.Config.DEFAULT_BASE_ADDRESS

    abstract val chatId: Long
    abstract val initialState: State
    open val callbackQueryListenAddress: String
        get() = VxUtilAddress.Private.withClassifier(deploymentID, VertigramAddress.Dialog.Classifier.CallbackQuery)
    open val messageListenAddress: String
        get() = VxUtilAddress.Private.withClassifier(deploymentID, VertigramAddress.Dialog.Classifier.Message)

    protected open val handleCancel: Boolean = false
    protected open val handleRollback: Boolean = false

    protected open val timeout: Duration? = null
    protected open val cancelState: State = silentCancelled()
    protected open val timeoutState: State = silentTimeout()
    protected open val historySize: Int = 100
    protected open val defaultHistoryBehavior: HistoryBehavior = HistoryBehavior.PUSH

    private val history: ArrayDeque<State> = ArrayDeque()
    @PublishedApi
    internal val mutex = Mutex()

    protected val tg: Telegram by lazy {
        TgVTelegram(vertx, tgVAddressBase)
    }

    protected var state: State? = null
    private var prevMsgInfo: MsgInfo? = null
    private var msgInfo: MsgInfo? = null
    private var timeoutTimerHandle: Job? = null

    override suspend fun start() {
        super.start()
        becomeWithLock(initialState, defaultHistoryBehavior)
        suspendJsonConsumer(callbackQueryListenAddress, function = ::handleCallbackQuery)
        suspendJsonConsumer(messageListenAddress, function = ::handleMessage)
        scheduleTimeout()
    }

    protected suspend inline fun withLock(block: () -> Unit) {
        val oldState = state
        mutex.withLock {
            if (state != oldState) return
            block()
        }
    }

    private suspend fun handleCallbackQuery(callbackQuery: CallbackQuery) = messageHandler {
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


    protected fun canRollback() = history.isNotEmpty()
    protected suspend fun rollback() {
        val state = history.removeLast()
        become(state, HistoryBehavior.SKIP)
    }

    protected suspend fun sendOrEdit(text: Markdown, buttons: InlineKeyboardMarkup? = null, forceSend: Boolean = false) {
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
                text = text.toString(),
                parseMode = ParseMode.MARKDOWN_V2,
                replyMarkup = buttons
            ).messageId
            this.msgInfo = MsgInfo(msgId, buttons != null, text.toString(), buttons)
        } else if (text.toString() != msgInfo.text || buttons != msgInfo.markup) {
            val msgId = tg.editMessageText(
                chatId = chatId.toChatId(),
                messageId = msgInfo.id,
                text = text.toString(),
                parseMode = ParseMode.MARKDOWN_V2,
                replyMarkup = buttons
            ).messageId
            this.msgInfo = MsgInfo(msgId, buttons != null, text.toString(), buttons)
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

    abstract class State(@PublishedApi internal val v: AbstractTelegramDialogVerticle) {
        protected suspend inline fun withLock(block: () -> Unit) {
            v.withLock(block)
        }
        open suspend fun handleCallbackQuery(callbackQuery: CallbackQuery) {}
        open suspend fun handleMessage(message: Message) {}
        open suspend fun onChildDeath(deathNotice: DeathNotice) {
            v.die(deathNotice.reason)
        }
        open suspend fun sideEffect() {}
        open fun boost(): State? = null

        protected suspend fun become(newState: State, historyBehavior: HistoryBehavior = v.defaultHistoryBehavior) {
            v.become(newState, historyBehavior)
        }

        protected suspend fun becomeWithLock(
            toState: State,
            historyBehavior: HistoryBehavior = v.defaultHistoryBehavior
        ) {
            v.becomeWithLock(toState, historyBehavior)
        }

        protected fun setTimerWithLock(duration: Duration, handler: suspend () -> Unit): Job =
            v.setTimerWithLock(duration, handler)

        protected fun canRollback() = v.canRollback()

        protected suspend fun rollback() {
            v.rollback()
        }

        protected fun die(reason: DeathReason) {
            v.die(reason)
        }

        protected fun complete() {
            v.complete()
        }

        protected fun fail() {
            v.fail()
        }

        protected fun cancel() {
            v.cancel()
        }

        protected suspend fun sendOrEdit(
            text: Markdown, buttons: InlineKeyboardMarkup? = null, forceSend: Boolean = false
        ) {
            v.sendOrEdit(text, buttons, forceSend)
        }

        protected val tg: Telegram
            get() = v.tg

        protected fun yawnTimeout(): State = YawnTimeout(v)
        protected fun silentTimeout(): State = SilentTimeout(v)
        protected fun crossCancelled(): State = CrossCancelled(v)
        protected fun silentCancelled(): State = SilentCancelled(v)
        protected fun checkmarkDone(): State = CheckmarkDone(v)
        protected fun silentDone(): State = SilentDone(v)

    }

    protected fun yawnTimeout(): State = YawnTimeout(this)
    protected fun silentTimeout(): State = SilentTimeout(this)
    protected fun crossCancelled(): State = CrossCancelled(this)
    protected fun silentCancelled(): State = SilentCancelled(this)
    protected fun checkmarkDone(): State = CheckmarkDone(this)
    protected fun silentDone(): State = SilentDone(this)

    private class YawnTimeout(private val verticle: AbstractTelegramDialogVerticle) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit(
                md {
                    +"\uD83E\uDD71"
                }
            )
            verticle.timeout()
        }
    }

    private class SilentTimeout(private val verticle: AbstractTelegramDialogVerticle) : State(verticle) {
        override suspend fun sideEffect() {
            verticle.timeout()
        }
    }

    private class CrossCancelled(private val verticle: AbstractTelegramDialogVerticle) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit(
                md {
                    +"❌"
                }
            )
            cancel()
        }
    }

    private class SilentCancelled(private val verticle: AbstractTelegramDialogVerticle) : State(verticle) {
        override suspend fun sideEffect() {
            cancel()
        }
    }

    private class CheckmarkDone(private val verticle: AbstractTelegramDialogVerticle) : State(verticle) {
        override suspend fun sideEffect() {
            sendOrEdit(
                md {
                    +"✅"
                }
            )
            complete()
        }
    }

    private class SilentDone(private val verticle: AbstractTelegramDialogVerticle) : State(verticle) {
        override suspend fun sideEffect() {
            complete()
        }
    }

    private data class MsgInfo(val id: Long, val hasButtons: Boolean, val text: String, val markup: ReplyMarkup?)

    companion object {
        const val CANCEL = "cancel"
        const val BACK = "back"
    }

    enum class HistoryBehavior {
        PUSH, REPLACE_LAST, SKIP, WIPE
    }
}
