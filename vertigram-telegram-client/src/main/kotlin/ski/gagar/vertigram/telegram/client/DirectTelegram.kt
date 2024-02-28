package ski.gagar.vertigram.telegram.client

import com.fasterxml.jackson.databind.JavaType
import io.vertx.core.Vertx
import io.vertx.core.net.ProxyOptions
import ski.gagar.vertigram.telegram.client.impl.TelegramImpl
import ski.gagar.vertigram.telegram.client.impl.TelegramImplOptions
import ski.gagar.vertigram.telegram.methods.TelegramCallable
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger
import java.time.Duration

private val LONG_POLL_DEFAULT_GAP: Duration = Duration.ofSeconds(5)

/**
 * Telegram client that uses direct HTTP connections.
 *
 * This implementation keeps HTTP connection pools open until it is closed.
 * This should be considered a "heavy" client, which means you probably should consider
 * sharing it across your app. The implemntation is thread-safe.
 */
class DirectTelegram(
    /**
     * Auth token
     */
    token: String,
    /**
     * Vertx instance
     */
    vertx: Vertx,
    /**
     * Options
     */
    private val options: Options = Options()
) : ski.gagar.vertigram.telegram.client.AbstractTelegram(), AutoCloseable {
    private val impl: TelegramImpl =
        TelegramImpl(
            token,
            vertx,
            options.proxy,
            TelegramImplOptions(
                tgBase = options.tgBase,
                shortPollTimeout = options.shortPollTimeout,
                longPollTimeout = options.longPollTimeout,
                pools = options.pools.toImpl()
            )
        )

     override suspend fun <T> call(
         resultType: JavaType,
         callable: TelegramCallable<T>
    ): T =
        impl.call(resultType, callable)

    @Suppress("DEPRECATION")
    override suspend fun getUpdates(offset: Long?, limit: Int?, allowedUpdates: List<Update.Type>): List<Update<*>> =
        impl.call(
            typeFactory.constructParametricType(List::class.java, Map::class.java),
            ski.gagar.vertigram.telegram.methods.GetUpdatesRaw(
                offset = offset,
                limit = limit,
                timeout = options.getUpdatesTimeoutParam,
                allowedUpdates = allowedUpdates
            ), longPoll = true
        ).mapNotNull { raw ->
            try {
                impl.mapper.convertValue(raw, Update::class.java)
            } catch (e: IllegalArgumentException) {
                logger.lazy.error(throwable = e) {
                    "Malformed update $raw"
                }
                val id = raw["update_id"] as? Long
                    ?: throw IllegalArgumentException("Malformed update $raw, there is no even update_id")
                Update.Malformed(
                    updateId = id,
                    malformedRawData = raw
                )
            }
        }

    override suspend fun downloadFile(path: String, outputPath: String) {
        impl.downloadFile(path, outputPath)
    }

    override fun close() {
        impl.close()
    }

    /**
     * [DirectTelegram] options
     */
    data class Options(
        /**
         * Base URL
         */
        val tgBase: String = "https://api.telegram.org",
        /**
         * Timeout for regular HTTP requests
         */
        val shortPollTimeout: Duration = Duration.ofSeconds(5),
        /**
         * Timeout for long poll `getUpdates` HTTP requests
         */
        val longPollTimeout: Duration = Duration.ofSeconds(60),
        /**
         * A value for `timeout` parameter for getUpdates method.
         *
         * By default, a value, slightly less than HTTP timeout for long-poll requests ([longPollTimeout])
         */
        val getUpdatesTimeoutParam: Duration = longPollTimeout - LONG_POLL_DEFAULT_GAP,
        /**
         * HTTP proxy options
         */
        val proxy: ProxyOptions? = null,
        /**
         * Connection pool options
         */
        val pools: Pools? = null

    ) {
        data class Pools(
            /**
             * Regular pool size. `null` means stick to default Vert.X value.
             */
            val regular: Int?,
            /**
             * Upload pool size. `null` means stick to default Vert.X value.
             */
            val upload: Int?,
            /**
             * Long-poll pool size. `null` means stick to default Vert.X value.
             */
            val longPoll: Int?,
            /**
             * Download pool size. `null` means stick to default Vert.X value.
             */
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


val THIN_POOLS = DirectTelegram.Options.Pools(1, 1, 1, 1)
fun DirectTelegram.Options?.withDefaultThinPool() =
    this?.copy(pools = pools ?: THIN_POOLS) ?: DirectTelegram.Options(pools = THIN_POOLS)
