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
import ski.gagar.vertigram.verticles.telegram.AbstractDispatchVerticle.DialogKey
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

/**
 * A verticle that does message dispatching to child verticles unique for given [DialogKey].
 *
 * Can be useful together with [StatefulTelegramDialogVerticle].
 *
 * For each [DialogKey] (e.g. `chatId`+`userId`) this verticle will spawn a child
 * (spawning should be implemented by subclass in [doStart]). If there is already a child with given [DialogKey],
 * it will pass the message or callback query to it.
 *
 * The spawned verticle can maintain its state given the condition that it receives messages only for a single dialog.
 */
abstract class AbstractDispatchVerticle<Config : AbstractDispatchVerticle.Config, DialogKey : AbstractDispatchVerticle.DialogKey> : AbstractHierarchyVerticle<Config>() {
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
     * Deploy a verticle to dispatch updates to and return [DialogDescriptor]
     *
     * To be overridden by subclass.
     */
    protected abstract suspend fun doStart(dialogKey: DialogKey, msg: Message): DialogDescriptor?

    override suspend fun start() {
        super.start()
        consumer<Message, Unit>(TelegramAddress.dispatchAddress(Update.Type.MESSAGE, typedConfig.baseAddress)) {
            handleMessage(it)
        }

        consumer<Update.CallbackQuery.Payload, Unit>(TelegramAddress.dispatchAddress(Update.Type.CALLBACK_QUERY, typedConfig.baseAddress)) {
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

    protected fun passMessageToOngoing(message: Message, desc: DialogDescriptor) {
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

    override suspend fun onChildDeath(deathNotice: DeathNotice) {
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
            fun chat(msg: Message): DialogKey? = msg.from ?.let { from ->
                Chat(chatId = msg.chat.id)
            }
            fun chat(q: Update.CallbackQuery.Payload): DialogKey? = q.message ?.let { msg ->
                Chat(chatId = msg.chat.id)
            }
        }
    }
}
