package ski.gagar.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Vertx
import io.vertx.core.net.ProxyOptions
import ski.gagar.vertigram.client.impl.TelegramImpl
import ski.gagar.vertigram.client.impl.TelegramImplOptions
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.methods.TelegramCallable
import ski.gagar.vertigram.types.MalformedUpdate
import ski.gagar.vertigram.types.ParsedUpdate
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.UpdateType
import java.time.Duration

private val LONG_POLL_DEFAULT_GAP: Duration = Duration.ofSeconds(5)

class DirectTelegram(
    token: String,
    vertx: Vertx,
    private val options: Options = Options()
) : AbstractTelegram(), AutoCloseable {
    @PublishedApi
    internal val impl: TelegramImpl =
        TelegramImpl(
            token,
            vertx,
            options.proxy,
            TelegramImplOptions(
                tgBase = options.tgBase,
                shortPollTimeout = options.shortPollTimeout,
                longPollTimeout = options.longPollTimeout
            )
        )

     override suspend fun <T> call(
        type: JavaType,
        callable: TelegramCallable<T>
    ): T =
        impl.call(type, callable)

    @Suppress("DEPRECATION")
    override suspend fun getUpdates(offset: Long?, limit: Int?, allowedUpdates: List<UpdateType>?): List<Update> =
        impl.call(
            typeFactory.constructParametricType(List::class.java, Map::class.java),
            ski.gagar.vertigram.methods.GetUpdatesRaw(
                offset = offset,
                limit = limit,
                timeout = options.getUpdatesTimeoutParam,
                allowedUpdates = allowedUpdates
            ), longPoll = true
        ).mapNotNull { raw ->
            try {
                impl.mapper.convertValue(raw, ParsedUpdate::class.java)
            } catch (e: IllegalArgumentException) {
                logger.lazy.error(throwable = e) {
                    "Malformed update $raw"
                }
                impl.mapper.convertValue(raw, MalformedUpdate::class.java)
            }
        }

    override suspend fun downloadFile(path: String, outputPath: String) {
        impl.downloadFile(path, outputPath)
    }

    override fun close() {
        impl.close()
    }

    data class Options(
        val tgBase: String = "https://api.telegram.org",
        val shortPollTimeout: Duration = Duration.ofSeconds(5),
        val longPollTimeout: Duration = Duration.ofSeconds(60),
        val getUpdatesTimeoutParam: Duration = longPollTimeout - LONG_POLL_DEFAULT_GAP,
        val proxy: ProxyOptions? = null,
        val pools: Pools? = null

    ) {
        data class Pools(
            val regular: Int?,
            val upload: Int?,
            val longPoll: Int?,
            val download: Int?
        )
    }
}

private fun DirectTelegram.Options.Pools?.toImpl() = TelegramImplOptions.Pools(
    regular = this?.regular,
    upload = this?.upload,
    longPoll = this?.longPoll,
    download = this?.download
)


val THIN_POOLS = DirectTelegram.Options.Pools(1, 0, 0, 0)
fun DirectTelegram.Options?.withDefaultThinPool() =
    this?.copy(pools = pools ?: THIN_POOLS) ?: DirectTelegram.Options(pools = THIN_POOLS)
