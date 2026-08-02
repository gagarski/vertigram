package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.launch
import ski.gagar.vertigram.awaitRegistration
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.markup.toFormattedText
import ski.gagar.vertigram.telegram.methods.sendMessage
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.util.toChatId
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.common.HierarchyVerticle
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason
import ski.gagar.vertigram.verticles.telegram.DispatchVerticle.DialogKey
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress
import java.time.Duration

/**
 * A verticle that does message dispatching to child verticles unique for given [DialogKey].
 *
 * Can be useful together with [StatefulTelegramDialogVerticle] or [SimpleTelegramDialogVerticle].
 *
 * For each [DialogKey] (e.g. `chatId`+`userId`) this verticle will spawn a child
 * (preparation should be implemented by subclasses in [prepareChild]). If there is already a child with given [DialogKey],
 * it will pass the message or callback query to it.
 *
 * The spawned verticle can maintain its state given the condition that it receives messages only for a single dialog.
 */
abstract class DispatchVerticle<C : DispatchVerticle.Config, VC> : HierarchyVerticle<C>() {
    protected val tg: Telegram by lazy {
        ThinTelegram(vertigram, typedConfig.verticleAddress)
    }

    private val dialogs = mutableMapOf<DialogKey, DialogState>()

    /**
     * [io.vertx.kotlin.coroutines.CoroutineVerticle.deploymentID] to active dialog map
     */
    private val dialogsInv = mutableMapOf<String, ActiveDialog>()
    private val pendingDeathNotices = mutableMapOf<String, DeathNotice>()

    /**
     * Period between cleanup attempts for unmatched child death notices. Must resolve to at least one millisecond.
     */
    protected open val pendingDeathNoticeCleanupPeriod: Duration = Duration.ofMinutes(1)

    /**
     * Create [DialogKey] from incoming [Message]
     *
     * To be overridden by subclass.
     */
    protected abstract fun dialogKey(msg: Message): DialogKey?

    /**
     * Create [DialogKey] from incoming [Update.CallbackQuery.Payload]
     *
     * To be overridden by subclass.
     */
    protected abstract fun dialogKey(q: Update.CallbackQuery.Payload): DialogKey?

    /**
     * Exctract chat id from [DialogKey].
     *
     * To be overridden by subclass.
     */
    protected open fun toChatId(key: DialogKey): Long? = key.chatId

    /**
     * Should [q] be handled.
     *
     * May be overridden by subclass, by default returns true
     */
    protected open suspend fun shouldHandleCallbackQuery(q: Update.CallbackQuery.Payload): Boolean = true

    /**
     * Should [msg] be handled
     *
     * May be overridden by subclass, by default returns true.
     */
    protected open suspend fun shouldHandleMessage(msg: Message): Boolean = true

    /**
     * Decide whether [msg] should start a dialog and prepare its child deployment.
     *
     * This function may suspend, and multiple calls for the same [dialogKey] may run concurrently. The returned
     * verticle is therefore only a deployment candidate: the dispatcher may discard it if another candidate claims
     * the dialog first. Implementations must not acquire resources that require cleanup before the verticle starts.
     */
    protected abstract suspend fun prepareChild(dialogKey: DialogKey, msg: Message): Deployment<VC>?

    override suspend fun start() {
        super.start()
        val cleanupPeriodMillis = pendingDeathNoticeCleanupPeriod.toMillis()
        require(cleanupPeriodMillis > 0) { "pendingDeathNoticeCleanupPeriod must be at least one millisecond" }
        vertx.setPeriodic(cleanupPeriodMillis) {
            clearPendingDeathNoticesIfIdle()
        }

        consumer<Message, Unit>(TelegramAddress.dispatchAddress(Update.Type.MESSAGE, typedConfig.baseAddress)) {
            handleMessage(it)
        }.awaitRegistration()

        consumer<Update.CallbackQuery.Payload, Unit>(TelegramAddress.dispatchAddress(Update.Type.CALLBACK_QUERY, typedConfig.baseAddress)) {
            handleCallbackQuery(it)
        }.awaitRegistration()
    }

    private suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) {
        val key = dialogKey(callbackQuery) ?: return
        if (dialogs[key] == null) return

        if (!shouldHandleCallbackQuery(callbackQuery))
            return

        when (val dialog = dialogs[key]) {
            is DialogState.Active -> passCallbackQueryToOngoing(callbackQuery, dialog)
            is DialogState.Starting -> dialog.pendingUpdates.add(PendingUpdate.CallbackQuery(callbackQuery))
            null -> Unit
        }
    }

    private suspend fun handleMessage(message: Message) {
        if (!shouldHandleMessage(message))
            return

        val dialogKey = dialogKey(message) ?: return
        when (val dialog = dialogs[dialogKey]) {
            is DialogState.Active -> {
                passMessageToOngoing(message, dialog)
                return
            }
            is DialogState.Starting -> {
                dialog.pendingUpdates.add(PendingUpdate.Message(message))
                return
            }
            null -> Unit
        }

        val deployment = prepareChild(dialogKey, message)

        when (val dialog = dialogs[dialogKey]) {
            is DialogState.Active -> passMessageToOngoing(message, dialog)
            is DialogState.Starting -> dialog.pendingUpdates.add(PendingUpdate.Message(message))
            null -> deployment?.let { startAndInitialize(dialogKey, message, it) }
        }
    }

    private suspend fun startAndInitialize(dialogKey: DialogKey, msg: Message, deployment: Deployment<VC>) {
        val starting = DialogState.Starting(mutableListOf(PendingUpdate.Message(msg)))
        dialogs[dialogKey] = starting

        try {
            val id = deployChild(deployment.verticle, deployment.config)
            check(dialogs[dialogKey] === starting) { "Dialog state changed while its child was being deployed" }

            val earlyDeathNotice = pendingDeathNotices.remove(id)
            if (earlyDeathNotice != null) {
                dialogs.remove(dialogKey)
                clearPendingDeathNoticesIfIdle()
                handleDialogDeath(dialogKey, earlyDeathNotice)
                return
            }

            val active = DialogState.Active(
                messageAddress = deployment.verticle.messageListenAddress,
                callbackQueryAddress = deployment.verticle.callbackQueryListenAddress
            )
            dialogs[dialogKey] = active
            dialogsInv[id] = ActiveDialog(dialogKey, active)
            clearPendingDeathNoticesIfIdle()
            starting.pendingUpdates.forEach { passToOngoing(it, active) }
        } catch (t: Throwable) {
            if (dialogs[dialogKey] === starting) {
                dialogs.remove(dialogKey)
            }
            clearPendingDeathNoticesIfIdle()
            throw t
        }
    }

    private fun passToOngoing(update: PendingUpdate, dialog: DialogState.Active) {
        when (update) {
            is PendingUpdate.Message -> passMessageToOngoing(update.payload, dialog)
            is PendingUpdate.CallbackQuery -> passCallbackQueryToOngoing(update.payload, dialog)
        }
    }

    private fun passMessageToOngoing(message: Message, dialog: DialogState.Active) {
        dialog.messageAddress?.let {
            vertigram.eventBus.send(
                it,
                message
            )
        }
    }

    private fun passCallbackQueryToOngoing(callbackQuery: Update.CallbackQuery.Payload, dialog: DialogState.Active) {
        dialog.callbackQueryAddress?.let {
            vertigram.eventBus.send(
                it,
                callbackQuery
            )
        }

    }

    override suspend fun onChildDeath(deathNotice: DeathNotice) {
        val activeDialog = dialogsInv.remove(deathNotice.id)
        if (activeDialog == null) {
            bufferDeathNotice(deathNotice)
            return
        }

        if (dialogs[activeDialog.key] !== activeDialog.state) return

        dialogs.remove(activeDialog.key)
        handleDialogDeath(activeDialog.key, deathNotice)
    }

    private fun bufferDeathNotice(deathNotice: DeathNotice) {
        pendingDeathNotices[deathNotice.id] = deathNotice
    }

    private fun clearPendingDeathNoticesIfIdle() {
        if (dialogs.values.any { it is DialogState.Starting }) return
        if (pendingDeathNotices.isEmpty()) return

        logger.lazy.warn {
            val notices = pendingDeathNotices.values.joinToString { "${it.id} (${it.reason})" }
            "Discarding ${pendingDeathNotices.size} martian child death notice(s): $notices"
        }
        pendingDeathNotices.clear()
    }

    private fun handleDialogDeath(key: DialogKey, deathNotice: DeathNotice) {
        val chatId = toChatId(key)

        if (deathNotice.reason == DeathReason.FAILED && null != chatId) {
            launch {
                tg.sendMessage(
                    text = "Something went wrong".toFormattedText(),
                    chatId = chatId.toChatId(),
                )
            }
        }
    }

    private sealed interface DialogState {
        class Starting(val pendingUpdates: MutableList<PendingUpdate>) : DialogState
        class Active(val messageAddress: String?, val callbackQueryAddress: String?) : DialogState
    }

    private sealed interface PendingUpdate {
        data class Message(val payload: ski.gagar.vertigram.telegram.types.Message) : PendingUpdate
        data class CallbackQuery(val payload: Update.CallbackQuery.Payload) : PendingUpdate
    }

    private data class ActiveDialog(val key: DialogKey, val state: DialogState.Active)

    /**
     * Base interface for verticle configuration
     */
    interface Config {
        /**
         * Base address to receive demultiplexed updates
         */
        val baseAddress: String

        /**
         * Telegram verticle base address
         */
        val verticleAddress: String
    }

    /**
     * Interface for DialogKey. Implementation must propely implement equals/hashCode.
     */
    interface DialogKey {
        val chatId: Long
        private data class ChatAndUser(override val chatId: Long, val userId: Long) : DialogKey
        private data class Chat(override val chatId: Long) : DialogKey

        companion object {
            fun chatAndUser(msg: Message): DialogKey? = msg.from ?.let { from ->
                ChatAndUser(chatId = msg.chat.id, userId = from.id)
            }
            fun chatAndUser(q: Update.CallbackQuery.Payload): DialogKey? = q.message ?.let { msg ->
                ChatAndUser(chatId = msg.chat.id, userId = q.from.id)
            }
            fun chat(msg: Message): DialogKey? = Chat(chatId = msg.chat.id)
            fun chat(q: Update.CallbackQuery.Payload): DialogKey? = q.message ?.let { msg ->
                Chat(chatId = msg.chat.id)
            }
        }
    }

    data class Deployment<VC>(
        val verticle: TelegramDialogVerticle<VC>,
        val config: VC
    )

    /**
     * [DispatchVerticle] that dispatches updates by chat id + user id
     */
    abstract class ByChatAndUser<C : Config, VC> : DispatchVerticle<C, VC>() {
        override fun dialogKey(msg: Message): DialogKey? = DialogKey.chatAndUser(msg)
        override fun dialogKey(q: Update.CallbackQuery.Payload): DialogKey? = DialogKey.chatAndUser(q)
    }

    /**
     * [DispatchVerticle] that dispatches updates by chat id
     */
    abstract class ByChat<C : Config, VC> : DispatchVerticle<C, VC>() {
        override fun dialogKey(msg: Message): DialogKey? = DialogKey.chat(msg)
        override fun dialogKey(q: Update.CallbackQuery.Payload): DialogKey? = DialogKey.chat(q)
    }
}
