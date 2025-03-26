package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.client.ThinTelegram
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

abstract class SimpleTelegramDialogVerticle<Config> : TelegramDialogVerticle<Config>() {
    /**
     * Base address for [TelegramVerticle] (used with [tg])
     *
     * May be overridden by subclasses
     */
    open val telegramAddressBase = TelegramAddress.TELEGRAM_VERTICLE_BASE

    /**
     * Telegram client
     */
    protected val tg: Telegram by lazy {
        ThinTelegram(vertigram, telegramAddressBase)
    }

    @PublishedApi
    internal val mutex = Mutex()

    /**
     * Callback query handler
     */
    protected open suspend fun handleCallbackQuery(callbackQuery: Update.CallbackQuery.Payload) {}

    /**
     * Message handler
     */
    protected open suspend fun handleMessage(message: Message) {}

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

    override suspend fun start() {
        super.start()
        callbackQueryListenAddress?.let {
            consumer(it, function = ::handleCallbackQuery)
        }
        messageListenAddress?.let {
            consumer(it, function = ::handleMessage)
        }
    }
}