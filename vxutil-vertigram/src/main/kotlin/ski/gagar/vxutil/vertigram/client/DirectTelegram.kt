package ski.gagar.vxutil.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Vertx
import io.vertx.core.net.ProxyOptions
import ski.gagar.vxutil.vertigram.client.impl.TelegramImpl
import ski.gagar.vxutil.vertigram.client.impl.TelegramImplOptions
import ski.gagar.vxutil.vertigram.types.MalformedUpdate
import ski.gagar.vxutil.vertigram.types.ParsedUpdate
import ski.gagar.vxutil.vertigram.types.Update
import ski.gagar.vxutil.vertigram.methods.GetUpdatesRaw
import ski.gagar.vxutil.vertigram.methods.TgCallable
import java.io.Closeable
import java.time.Duration

private val LONG_POLL_DEFAULT_GAP: Duration = Duration.ofSeconds(5)

class DirectTelegram(
    token: String,
    vertx: Vertx,
    private val options: Options = Options()
) : Telegram(), Closeable {
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
        callable: TgCallable<T>
    ): T =
        impl.call(type, callable)

    @Suppress("DEPRECATION")
    override suspend fun getUpdates(offset: Long?, limit: Int?): List<Update> =
        impl.call(
            typeFactory.constructParametricType(List::class.java, Map::class.java),
            GetUpdatesRaw(
                offset = offset,
                limit = limit,
                timeout = options.getUpdatesTimeoutParam
            ), longPoll = true
        ).mapNotNull { raw ->
            try {
                impl.mapper.convertValue(raw, ParsedUpdate::class.java)
            } catch (e: IllegalArgumentException) {
                // If this one throws, we give up
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
        val proxy: ProxyOptions? = null
    )
}
