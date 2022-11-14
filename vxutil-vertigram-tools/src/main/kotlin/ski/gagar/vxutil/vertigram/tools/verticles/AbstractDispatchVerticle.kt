package ski.gagar.vxutil.vertigram.tools.verticles

import kotlinx.coroutines.launch
import ski.gagar.vxutil.ignore
import ski.gagar.vxutil.jackson.requestJsonAwait
import ski.gagar.vxutil.jackson.suspendJsonConsumer
import ski.gagar.vxutil.verticles.children.AbstractHierarchyVerticle
import ski.gagar.vxutil.verticles.children.messages.DeathNotice
import ski.gagar.vxutil.verticles.children.messages.DeathReason
import ski.gagar.vxutil.vertigram.client.Telegram
import ski.gagar.vxutil.vertigram.client.TgVTelegram
import ski.gagar.vxutil.vertigram.sendMessage
import ski.gagar.vxutil.vertigram.tools.verticles.address.VertigramAddress
import ski.gagar.vxutil.vertigram.types.CallbackQuery
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.toChatId
import ski.gagar.vxutil.vertigram.builders.md
import ski.gagar.vxutil.vertigram.verticles.TelegramVerticle

abstract class AbstractDispatchVerticle<DialogKey> : AbstractHierarchyVerticle() {
    open val tgVAddressBase = TelegramVerticle.Config.DEFAULT_BASE_ADDRESS
    protected val tg: Telegram by lazy {
        TgVTelegram(vertx, tgVAddressBase)
    }


    protected val dialogs = mutableMapOf<DialogKey, DialogDescriptor>()
    protected val dialogsInv = mutableMapOf<String, DialogKey>()

    protected abstract fun dialogKey(msg: Message): DialogKey?
    protected abstract fun dialogKey(q: CallbackQuery): DialogKey?
    protected abstract fun toChatId(key: DialogKey): Long?
    protected abstract suspend fun shouldHandleCallbackQuery(q: CallbackQuery): Boolean
    protected abstract suspend fun shouldHandleMessage(msg: Message): Boolean
    protected abstract suspend fun doStart(dialogKey: DialogKey, msg: Message): DialogDescriptor?

    override suspend fun start() {
        super.start()
        suspendJsonConsumer<Message, Unit>(VertigramAddress.Message) {
            handleMessage(it)
        }

        suspendJsonConsumer<CallbackQuery, Unit>(VertigramAddress.CallbackQuery) {
            handleCallbackQuery(it)
        }
    }

    private suspend fun handleCallbackQuery(callbackQuery: CallbackQuery) {
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
        ignore(
            vertx.eventBus().requestJsonAwait(
                desc.messageAddress,
                message
            )
        )
    }

    private suspend fun passCallbackQueryToOngoing(callbackQuery: CallbackQuery, desc: DialogDescriptor) {
        ignore(
            vertx.eventBus().requestJsonAwait(
                desc.callbackQueryAddress,
                callbackQuery
            )
        )
    }

    override fun onChildDeath(deathNotice: DeathNotice) {
        val key = dialogsInv[deathNotice.id] ?: return
        val chatId = toChatId(key)

        if (deathNotice.reason == DeathReason.FAILED && null != chatId) {
            launch {
                tg.sendMessage(
                    text = md { +"Something went wrong" }.toString(),
                    chatId = chatId.toChatId(),
                    parseMode = ParseMode.MARKDOWN_V2
                )
            }
        }

        dialogsInv.remove(deathNotice.id)
        dialogs.remove(key)
    }

    data class DialogDescriptor(val id: String, val messageAddress: String, val callbackQueryAddress: String)

}
