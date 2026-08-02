package ski.gagar.vertigram.verticles.telegram

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ski.gagar.vertigram.awaitRegistration
import ski.gagar.vertigram.telegram.client.DirectTelegram
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.types.methods.TelegramCallable
import ski.gagar.vertigram.telegram.throttling.ThrottlingOptions
import ski.gagar.vertigram.telegram.throttling.ThrottlingTelegram
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.UpdateList
import ski.gagar.vertigram.util.VertigramTypeHints
import ski.gagar.vertigram.util.TelegramCallableTransport
import ski.gagar.vertigram.util.getOrAssert
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import ski.gagar.vertigram.verticles.telegram.TelegramVerticle.DownloadFile
import ski.gagar.vertigram.verticles.telegram.TelegramVerticle.GetUpdates
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

/**
 * A verticle, wrapping a [Telegram] client.
 *
 * The best way to talk to it is using [ski.gagar.vertigram.telegram.client.ThinTelegram] client.
 *
 * The messaging protocol is:
 *  - Consumers use the [ski.gagar.vertigram.Vertigram] protocol on top of the Vert.x event bus.
 *  - A Telegram method consumer has the address
 *    `<baseAddress>.<methodAddress>.<transport>`.
 *  - [Config.baseAddress] defaults to [TelegramAddress.TELEGRAM_VERTICLE_BASE].
 *  - `methodAddress` is either the explicit `TelegramCodegen.Method.verticleConsumerName` or, by default, the
 *    callable's simple class name. Each nesting segment starts with a lowercase letter and nested classes are separated
 *    by dots: `EditMessageCaption.InlineMessage` becomes `editMessageCaption.inlineMessage`.
 *  - `transport` is `json` or `multipart`, matching the callable's HTTP transport. For example,
 *    `AddStickerToSet` uses
 *    `ski.gagar.vertigram.telegram.verticle.addStickerToSet.multipart` with the default base address.
 *  - Each consumer accepts a Vertigram request whose payload is the corresponding
 *    [ski.gagar.vertigram.telegram.types.methods.TelegramCallable].
 *  - Each consumer returns a Vertigram response whose payload is the corresponding method return type.
 *  - `getUpdates` is a special case: it consumes [GetUpdates] payload and returns a list of updates as a payload
 *    in the response.
 *  - `downloadFile` is a special case: it consumes [DownloadFile] payload and returns an empty-payload response.
 */
class TelegramVerticle : VertigramVerticle<TelegramVerticle.Config>() {
    private lateinit var tg: Telegram

    override suspend fun start() {
        val directTg = DirectTelegram(
            typedConfig.token,
            vertx,
            typedConfig.telegramOptions
        )
        val throttling = typedConfig.throttling
        tg = if (null == throttling) {
            directTg
        } else {
            ThrottlingTelegram(vertx, directTg, throttling)
        }

        val consumers = buildList {
            add(
                consumer(
                    typedConfig.updatesAddress(), function = ::handleGetUpdates
                )
            )

            for ((tgvAddress, descriptor) in VertigramTypeHints.descriptorByTgvAddress) {
                if (!descriptor.generateVerticleConsumer)
                    continue
                add(
                    consumer(
                        typedConfig.callAddress(
                            tgvAddress,
                            RequestType.byTransport(descriptor.transport)
                        ),
                        requestJavaType = descriptor.requestType
                    ) { msg: TelegramCallable<*> ->
                        @Suppress("DEPRECATION")
                        tg.call(msg)
                    }
                )
            }

            add(consumer(typedConfig.longPollTimeoutAddress(), function = ::handleLongPollTimeout))
            add(consumer(typedConfig.downloadFileAddress(), function = ::handleDownloadFile))
        }

        coroutineScope {
            consumers.map { async { it.awaitRegistration() } }.awaitAll()
        }
    }

    override suspend fun stop() {
        val tg = this.tg
        if (tg is AutoCloseable) {
            tg.close()
        }
    }

    private suspend fun handleGetUpdates(msg: GetUpdates) =
        UpdateList(tg.getUpdates(limit = msg.limit, offset = msg.offset, allowedUpdates = msg.allowedUpdates))

    private fun handleLongPollTimeout(
        @Suppress("UNUSED_PARAMETER") msg: GetLongPollTimeout
    ) = typedConfig.telegramOptions.longPollTimeout

    private suspend fun handleDownloadFile(msg: DownloadFile) = tg.downloadFile(msg.path, msg.outputPath)

    enum class RequestType(val postfix: String) {
        Json("json"),
        Multipart("multipart");

        companion object {
            fun byTransport(transport: TelegramCallableTransport) =
                when (transport) {
                    TelegramCallableTransport.JSON -> Json
                    TelegramCallableTransport.MULTIPART -> Multipart
                }

            fun <T : TelegramCallable<*>> byClass(clazz: Class<T>) =
                byTransport(
                    VertigramTypeHints.descriptorByCallable.getOrAssert(clazz).transport
                )

            fun byCallable(tgc: TelegramCallable<*>) =
                byClass(tgc.javaClass)
        }
    }

    /**
     * Config for [TelegramVerticle]
     */
    data class Config(
        /**
         * Access token
         */
        val token: String,
        /**
         * Base address prepended to every consumer address.
         */
        val baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE,
        /**
         * Options passed to [DirectTelegram]
         */
        val telegramOptions: DirectTelegram.Options = DirectTelegram.Options(),
        /**
         * Throttling options (`null` to disable throttling)
         */
        val throttling: ThrottlingOptions? = ThrottlingOptions()
    ) {

        internal fun callAddress(methodName: String, requestType: RequestType) =
            callAddress(
                methodName,
                baseAddress,
                requestType
            )

        /**
         * Address to call [Telegram.getUpdates]
         */
        fun updatesAddress() = updatesAddress(baseAddress)

        /**
         * Consumer address for [clazz]
         */
        fun <T : TelegramCallable<*>> callAddress(clazz: Class<T>) =
            callAddress(
                clazz,
                baseAddress
            )

        /**
         * Consumer address for [obj]
         */
        inline fun <reified T: TelegramCallable<*>> callAddress(obj: T) =
            callAddress(
                obj,
                baseAddress
            )

        /**
         * Address to fetch long poll timeout (used by [ski.gagar.vertigram.telegram.client.ThinTelegram])
         */
        fun longPollTimeoutAddress() =
            longPollTimeoutAddress(
                baseAddress
            )

        /**
         * Address to call [Telegram.downloadFile]
         */
        fun downloadFileAddress() =
            downloadFileAddress(baseAddress)

        companion object {
            const val GET_UPDATES = "getUpdates"

            private fun callAddress(methodName: String,
                                    baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE,
                                    requestType: RequestType
            ) =
                "$baseAddress.$methodName.${requestType.postfix}"

            /**
             * Address to call [Telegram.getUpdates]
             */
            fun updatesAddress(baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE) =
                callAddress(GET_UPDATES, baseAddress, RequestType.Json)

            fun <T : TelegramCallable<*>> callAddress(
                clazz: Class<T>,
                baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE
            ): String {
                val descriptor = VertigramTypeHints.descriptorByCallable.getOrAssert(clazz)
                return callAddress(
                    descriptor.tgvAddress,
                    baseAddress,
                    RequestType.byTransport(descriptor.transport)
                )
            }

            /**
             * Consumer address for [obj]
             */
            fun <T: TelegramCallable<*>> callAddress(
                obj: T,
                baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE
            ): String {
                val descriptor = VertigramTypeHints.descriptorByCallable.getOrAssert(obj.javaClass)
                return callAddress(
                    descriptor.tgvAddress,
                    baseAddress,
                    RequestType.byTransport(descriptor.transport)
                )
            }

            /**
             * Address to fetch long poll timeout (used by [ski.gagar.vertigram.telegram.client.ThinTelegram])
             */
            fun longPollTimeoutAddress(baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE) =
                "$baseAddress.conf.longPollTimeout"

            /**
             * Address to call [Telegram.downloadFile]
             */
            fun downloadFileAddress(baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE) =
                "$baseAddress.conf.downloadFile"

        }
    }

    /**
     * Message to call [Telegram.getUpdates]
     */
    data class GetUpdates(val offset: Long?, val limit: Int?, val allowedUpdates: List<Update.Type>)

    /**
     * Message to fetch long poll timeout (used by [ski.gagar.vertigram.telegram.client.ThinTelegram])
     */
    object GetLongPollTimeout

    /**
     * Message to call [Telegram.downloadFile]
     */
    data class DownloadFile(val path: String, val outputPath: String)
}
