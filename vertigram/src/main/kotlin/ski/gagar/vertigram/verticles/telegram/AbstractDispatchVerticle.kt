package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.launch
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.markup.toRichText
import ski.gagar.vertigram.telegram.methods.sendMessage
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.util.toChatId
import ski.gagar.vertigram.verticles.common.AbstractHierarchyVerticle
import ski.gagar.vertigram.verticles.common.messages.DeathNotice
import ski.gagar.vertigram.verticles.common.messages.DeathReason
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

/**
 * A verticle that does message dispatching to child verticles unique for given [DialogKey].
 *
 * Can be useful together with [AbstractTelegramDialogVerticle].
 *
 * For each [DialogKey] (e.g. `chatId`+`userId`) this verticle will spawn a child
 * (spawning should be implemented by subclass in [doStart]). If there is already a child with given [DialogKey],
 * it will pass the message or callback query to it.
 *
 * The spawned verticle can maintain its state given the condition that it receives messages only for a single dialog.
 */
abstract class AbstractDispatchVerticle<Config : AbstractDispatchVerticle.Config, DialogKey> : AbstractHierarchyVerticle<Config>() {
    protected val tg: Telegram by lazy {
        ThinTelegram(vertigram, typedConfig.verticleAddress)
    }

    /**
     * [DialogKey] to [DialogDescriptor] map
     */
    protected val dialogs = mutableMapOf<DialogKey, DialogDescriptor>()

    /**
     * [io.vertx.kotlin.coroutines.CoroutineVerticle.deploymentID] to [DialogKey] map
     */
    protected val dialogsInv = mutableMapOf<String, DialogKey>()

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
    protected abstract fun toChatId(key: DialogKey): Long?

    /**
     * Should [q] be handled.
     *
     * To be overridden by subclass.
     */
    protected abstract suspend fun shouldHandleCallbackQuery(q: Update.CallbackQuery.Payload): Boolean

    /**
     * Should [msg] be handled
     *
     * To be overridden by subclass.
     */
    protected abstract suspend fun shouldHandleMessage(msg: Message): Boolean

    /**
     * Deploy a verticle to dispatch updates to and return [DialogDescriptor]
     *
     * To be overridden by subclass.
     */
    protected abstract suspend fun doStart(dialogKey: DialogKey, msg: Message): DialogDescriptor?

    override suspend fun start() {
        super.start()
        consumer<Message, Unit>(TelegramAddress.demuxAddress(Update.Type.MESSAGE, typedConfig.baseAddress)) {
            handleMessage(it)
        }

        consumer<Update.CallbackQuery.Payload, Unit>(TelegramAddress.demuxAddress(Update.Type.CALLBACK_QUERY, typedConfig.baseAddress)) {
            handleCallbackQuery(it)
        }
    }

    private suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) {
        val key = dialogKey(callbackQuery) ?: return
        val child = dialogs[key] ?: return

        if (!shouldHandleCallbackQuery(callbackQuery))
            return

        passCallbackQueryToOngoing(callbackQuery, child)
    }

    private suspend fun handleMessage(message: Message) {
        if (!shouldHandleMessage(message))
            return

        val dialogKey = dialogKey(message) ?: return
        val ongoing = dialogs[dialogKey] // after suspending
        if (ongoing == null) {
            startAndInitialize(dialogKey, message)
        } else {
            passMessageToOngoing(message, dialogs[dialogKey]!!)
        }
    }

    private suspend fun startAndInitialize(dialogKey: DialogKey, msg: Message) {
        val desc = doStart(dialogKey, msg) ?: return
        dialogs[dialogKey] = desc
        dialogsInv[desc.id] = dialogKey
    }

    private fun passMessageToOngoing(message: Message, desc: DialogDescriptor) {
        vertigram.eventBus.send(
            desc.messageAddress,
            message
        )
    }

    private fun passCallbackQueryToOngoing(callbackQuery: Update.CallbackQuery.Payload, desc: DialogDescriptor) {
        vertigram.eventBus.send(
            desc.callbackQueryAddress,
            callbackQuery
        )
    }

    override fun onChildDeath(deathNotice: DeathNotice) {
        val key = dialogsInv[deathNotice.id] ?: return
        val chatId = toChatId(key)

        if (deathNotice.reason == DeathReason.FAILED && null != chatId) {
            launch {
                tg.sendMessage(
                    richText = "Something went wrong".toRichText(),
                    chatId = chatId.toChatId(),
                )
            }
        }

        dialogsInv.remove(deathNotice.id)
        dialogs.remove(key)
    }

    /**
     * Dialog descriptor returned by [doStart]
     */
    data class DialogDescriptor(val id: String, val messageAddress: String, val callbackQueryAddress: String)

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
}
