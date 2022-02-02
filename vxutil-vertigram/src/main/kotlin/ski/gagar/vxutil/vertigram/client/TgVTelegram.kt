package ski.gagar.vxutil.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import ski.gagar.vxutil.ignore
import ski.gagar.vxutil.jackson.ReplyException
import ski.gagar.vxutil.jackson.requestJsonAwait
import ski.gagar.vxutil.vertigram.entities.Update
import ski.gagar.vxutil.vertigram.entities.requests.GetUpdates
import ski.gagar.vxutil.vertigram.entities.requests.TgCallable
import ski.gagar.vxutil.vertigram.util.TypeHints
import ski.gagar.vxutil.vertigram.util.getOrAssert
import ski.gagar.vxutil.vertigram.verticles.TelegramVerticle


class TgVTelegram(
    private val vertx: Vertx,
    private val baseAddress: String = TelegramVerticle.Config.DEFAULT_BASE_ADDRESS,
    private val timeoutGap: Long = 5000L
) : Telegram() {

    private lateinit var longPollDeliveryOptions: DeliveryOptions

    private suspend fun getLongPollDeliveryOptions(): DeliveryOptions {
        if (::longPollDeliveryOptions.isInitialized)
            return longPollDeliveryOptions

        val timeout: Long = vertx.eventBus().requestJsonAwait(
            TelegramVerticle.Config.longPollTimeoutAddress(baseAddress),
            TelegramVerticle.GetLongPollTimeout
        )

        longPollDeliveryOptions = DeliveryOptions().setSendTimeout(timeoutGap + timeout)
        return longPollDeliveryOptions
    }

    override suspend fun getUpdates(offset: Long?, limit: Long?): List<Update> =
        try {
            @Suppress("DEPRECATION")
            vertx.eventBus().requestJsonAwait(
                TelegramVerticle.Config.callAddress(GetUpdates::class.java, baseAddress),
                TelegramVerticle.GetUpdates(offset, limit),
                TypeHints.returnTypesByClass.getOrAssert(GetUpdates::class.java),
                options = getLongPollDeliveryOptions()
            )
        } catch (ex: ReplyException) {
            throw ex.cause
        }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> call(type: JavaType, callable: TgCallable<T>): T =
        try {
            vertx.eventBus()
                .requestJsonAwait<TgCallable<T>, Any?>(
                    TelegramVerticle.Config.callAddress(callable, baseAddress),
                    callable,
                    type
                ) as T
        } catch (ex: ReplyException) {
            throw ex.cause
        }

    override suspend fun downloadFile(path: String, outputPath: String) {
        try {
            ignore(vertx.eventBus().requestJsonAwait(
                TelegramVerticle.Config.downloadFileAddress(baseAddress),
                TelegramVerticle.DownloadFile(path, outputPath)
            ))
        } catch (ex: ReplyException) {
            throw ex.cause
        }
    }
}
