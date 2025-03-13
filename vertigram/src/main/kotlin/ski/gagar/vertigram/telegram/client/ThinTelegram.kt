package ski.gagar.vertigram.telegram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.eventbus.DeliveryOptions
import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.telegram.types.methods.TelegramCallable
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.UpdateList
import ski.gagar.vertigram.util.TELEGRAM_TYPE_FACTORY
import ski.gagar.vertigram.verticles.telegram.TelegramVerticle
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress
import java.time.Duration

/**
 * A [Telegram] implementation using [TelegramVerticle] for making requests.
 *
 * @param vertigram [Vertigram] instance
 * @param baseAddress Base address for sending [Vertigram.EventBus.request]
 * @param timeoutGap A gap for [DeliveryOptions.timeout] when doing long-poll for [getUpdates].
 *     The [Vertigram.EventBus.request] for [getUpdates] will time out after long poll timeout set by verticle +
 *     `timeoutGap`
 */
class ThinTelegram(
    private val vertigram: Vertigram,
    private val baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE,
    private val timeoutGap: Duration = Duration.ofSeconds(5)
) : AbstractTelegram() {

    private lateinit var longPollDeliveryOptions: DeliveryOptions

    private suspend fun getLongPollDeliveryOptions(): DeliveryOptions {
        if (::longPollDeliveryOptions.isInitialized)
            return longPollDeliveryOptions

        val timeout: Duration = vertigram.eventBus.request(
            TelegramVerticle.Config.longPollTimeoutAddress(baseAddress),
            TelegramVerticle.GetLongPollTimeout
        )

        longPollDeliveryOptions = DeliveryOptions().setSendTimeout((timeoutGap + timeout).toMillis())
        return longPollDeliveryOptions
    }

    override suspend fun getUpdates(offset: Long?, limit: Int?, allowedUpdates: List<Update.Type>): List<Update<*>> =
        vertigram.eventBus.request(
            TelegramVerticle.Config.updatesAddress(baseAddress),
            TelegramVerticle.GetUpdates(offset, limit, allowedUpdates),
            UPDATE_LIST_TYPE,
            options = getLongPollDeliveryOptions()
        )

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> call(resultType: JavaType, callable: TelegramCallable<T>): T =
        vertigram.eventBus
            .request<TelegramCallable<T>, Any?>(
                TelegramVerticle.Config.callAddress(callable, baseAddress),
                callable,
                resultType
            ) as T

    override suspend fun downloadFile(path: String, outputPath: String) {
        vertigram.eventBus.send(
            TelegramVerticle.Config.downloadFileAddress(baseAddress),
            TelegramVerticle.DownloadFile(path, outputPath)
        )
    }

    companion object {
        private val UPDATE_LIST_TYPE = TELEGRAM_TYPE_FACTORY.constructType(UpdateList::class.java)
    }
}
