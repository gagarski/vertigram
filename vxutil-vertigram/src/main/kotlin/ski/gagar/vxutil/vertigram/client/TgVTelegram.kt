package ski.gagar.vxutil.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import ski.gagar.vxutil.ignore
import ski.gagar.vxutil.jackson.ReplyException
import ski.gagar.vxutil.jackson.requestJsonAwait
import ski.gagar.vxutil.vertigram.methods.TgCallable
import ski.gagar.vxutil.vertigram.types.Update
import ski.gagar.vxutil.vertigram.types.UpdateType
import ski.gagar.vxutil.vertigram.util.TypeHints
import ski.gagar.vxutil.vertigram.util.getOrAssert
import ski.gagar.vxutil.vertigram.verticles.TelegramVerticle
import java.time.Duration


class TgVTelegram(
    private val vertx: Vertx,
    private val baseAddress: String = TelegramVerticle.Config.DEFAULT_BASE_ADDRESS,
    private val timeoutGap: Duration = Duration.ofSeconds(5)
) : AbstractTelegram() {

    private lateinit var longPollDeliveryOptions: DeliveryOptions

    private suspend fun getLongPollDeliveryOptions(): DeliveryOptions {
        if (::longPollDeliveryOptions.isInitialized)
            return longPollDeliveryOptions

        val timeout: Duration = vertx.eventBus().requestJsonAwait(
            TelegramVerticle.Config.longPollTimeoutAddress(baseAddress),
            TelegramVerticle.GetLongPollTimeout
        )

        longPollDeliveryOptions = DeliveryOptions().setSendTimeout((timeoutGap + timeout).toMillis())
        return longPollDeliveryOptions
    }

    override suspend fun getUpdates(offset: Long?, limit: Int?, allowedUpdates: List<UpdateType>?): List<Update> =
        try {
            @Suppress("DEPRECATION")
            vertx.eventBus().requestJsonAwait(
                TelegramVerticle.Config.callAddress(ski.gagar.vxutil.vertigram.methods.GetUpdates::class.java, baseAddress),
                TelegramVerticle.GetUpdates(offset, limit, allowedUpdates),
                TypeHints.returnTypesByClass.getOrAssert(ski.gagar.vxutil.vertigram.methods.GetUpdates::class.java),
                options = getLongPollDeliveryOptions()
            )
        } catch (ex: ReplyException) {
            ex.unwrap()
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
            ex.unwrap()
        }

    override suspend fun downloadFile(path: String, outputPath: String) {
        try {
            ignore(vertx.eventBus().requestJsonAwait(
                TelegramVerticle.Config.downloadFileAddress(baseAddress),
                TelegramVerticle.DownloadFile(path, outputPath)
            ))
        } catch (ex: ReplyException) {
            ex.unwrap()
        }
    }

    override fun close() {}
}
