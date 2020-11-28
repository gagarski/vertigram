package ski.gagar.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Vertx
import io.vertx.core.file.AsyncFile
import io.vertx.core.net.ProxyOptions
import ski.gagar.vertigram.client.impl.TelegramImpl
import ski.gagar.vertigram.client.impl.TelegramImplOptions
import ski.gagar.vertigram.entities.Update
import ski.gagar.vertigram.entities.requests.GetUpdates
import ski.gagar.vertigram.entities.requests.TgCallable
import java.io.Closeable

private const val LONG_POLL_DEFAULT_GAP: Long = 5000L

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
    override suspend fun getUpdates(offset: Long?, limit: Long?): List<Update> =
        impl.call(
            typeFactory.constructParametricType(List::class.java, Update::class.java), GetUpdates(
                offset = offset,
                limit = limit,
                timeout = options.getUpdatesTimeoutParamMs / 1000
            ), longPoll = true
        )

    override suspend fun downloadFile(path: String, outputPath: String) {
        impl.downloadFile(path, outputPath)
    }

    override fun close() {
        impl.close()
    }

    data class Options(
        val tgBase: String = "https://api.telegram.org",
        val shortPollTimeout: Long = 5000L,
        val longPollTimeout: Long = 60000L,
        val getUpdatesTimeoutParamMs: Long = longPollTimeout - LONG_POLL_DEFAULT_GAP,
        val proxy: ProxyOptions? = null
    )
}
