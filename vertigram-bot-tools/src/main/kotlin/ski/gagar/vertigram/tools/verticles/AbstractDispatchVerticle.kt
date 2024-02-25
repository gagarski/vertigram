package ski.gagar.vertigram.tools.verticles

import kotlinx.coroutines.launch
import ski.gagar.vertigram.client.Telegram
import ski.gagar.vertigram.client.TgVTelegram
import ski.gagar.vertigram.markup.toRichText
import ski.gagar.vertigram.methods.sendMessage
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.util.toChatId
import ski.gagar.vertigram.verticles.VertigramAddresses
import ski.gagar.vertigram.verticles.children.AbstractHierarchyVerticle
import ski.gagar.vertigram.verticles.children.messages.DeathNotice
import ski.gagar.vertigram.verticles.children.messages.DeathReason

abstract class AbstractDispatchVerticle<Config, DialogKey> : AbstractHierarchyVerticle<Config>() {
    open val tgVAddressBase = VertigramAddresses.TELEGRAM_VERTICLE_BASE
    protected val tg: Telegram by lazy {
        TgVTelegram(vertigram, tgVAddressBase)
    }


    protected val dialogs = mutableMapOf<DialogKey, DialogDescriptor>()
    protected val dialogsInv = mutableMapOf<String, DialogKey>()

    protected abstract fun dialogKey(msg: Message): DialogKey?
    protected abstract fun dialogKey(q: Update.CallbackQuery.Payload): DialogKey?
    protected abstract fun toChatId(key: DialogKey): Long?
    protected abstract suspend fun shouldHandleCallbackQuery(q: Update.CallbackQuery.Payload): Boolean
    protected abstract suspend fun shouldHandleMessage(msg: Message): Boolean
    protected abstract suspend fun doStart(dialogKey: DialogKey, msg: Message): DialogDescriptor?

    override suspend fun start() {
        super.start()
        consumer<Message, Unit>(VertigramAddresses.demuxAddress(Update.Type.MESSAGE)) {
            handleMessage(it)
        }

        consumer<Update.CallbackQuery.Payload, Unit>(VertigramAddresses.demuxAddress(Update.Type.CALLBACK_QUERY)) {
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

    private suspend fun passMessageToOngoing(message: Message, desc: DialogDescriptor) {
        vertigram.eventBus.send(
            desc.messageAddress,
            message
        )
    }

    private suspend fun passCallbackQueryToOngoing(callbackQuery: Update.CallbackQuery.Payload, desc: DialogDescriptor) {
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

    data class DialogDescriptor(val id: String, val messageAddress: String, val callbackQueryAddress: String)

}
