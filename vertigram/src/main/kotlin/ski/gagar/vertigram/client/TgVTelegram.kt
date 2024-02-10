package ski.gagar.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Vertx
import io.vertx.core.eventbus.DeliveryOptions
import ski.gagar.vertigram.ignore
import ski.gagar.vertigram.jackson.ReplyException
import ski.gagar.vertigram.jackson.requestJsonAwait
import ski.gagar.vertigram.methods.TelegramCallable
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.UpdateList
import ski.gagar.vertigram.types.UpdateType
import ski.gagar.vertigram.util.TELEGRAM_TYPE_FACTORY
import ski.gagar.vertigram.verticles.TelegramVerticle
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
                TelegramVerticle.Config.updatesAddress(baseAddress),
                TelegramVerticle.GetUpdates(offset, limit, allowedUpdates),
                UPDATE_LIST_TYPE,
                options = getLongPollDeliveryOptions()
            )
        } catch (ex: ReplyException) {
            ex.unwrap()
        }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> call(type: JavaType, callable: TelegramCallable<T>): T =
        try {
            vertx.eventBus()
                .requestJsonAwait<TelegramCallable<T>, Any?>(
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

    companion object {
        private val UPDATE_LIST_TYPE = TELEGRAM_TYPE_FACTORY.constructType(UpdateList::class.java)
    }
}
