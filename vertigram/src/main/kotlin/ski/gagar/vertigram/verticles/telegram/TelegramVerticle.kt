package ski.gagar.vertigram.verticles.telegram

import com.fasterxml.jackson.core.type.TypeReference
import ski.gagar.vertigram.util.jackson.typeReference
import ski.gagar.vertigram.telegram.client.DirectTelegram
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.methods.JsonTelegramCallable
import ski.gagar.vertigram.telegram.methods.MultipartTelegramCallable
import ski.gagar.vertigram.telegram.methods.TelegramCallable
import ski.gagar.vertigram.telegram.throttling.ThrottlingOptions
import ski.gagar.vertigram.telegram.throttling.ThrottlingTelegram
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.UpdateList
import ski.gagar.vertigram.util.VertigramTypeHints
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
 * Otherwise, the general primciple of the verticle messagin protocol is the following:
 *  - The event bus consumers use [ski.gagar.vertigram.Vertigram] protocol on top of Vert.x event bus
 *  - The *base part* address of the consumer is either defined in
 *    [ski.gagar.vertigram.telegram.annotations.TelegramMethod.verticleConsumerName] or (by default) a class name
 *    from [ski.gagar.vertigram.telegram.methods],
 *    each part of which is uncapitalized: (e.g. `EditMessageCaption.InlineMessage` becomes
 *    `editMessageCaption.inlineMessage`)
 * - The *vertigram address* of the consumer is *base part* described above concatenated with postfix
 *   (.json or .multipart) based on content type used by the method when doing HTTP interaction with Telegram.
 *  - Each consumer consumes Vertigram requests with the payload of corresponding method (from
 *    [ski.gagar.vertigram.telegram.methods])
 *  - Each consumer returns Vertigram response with the payload of corresponding method return type
 *  - `getUpdates` is a special case: it consumes [GetUpdates] payload and returns a list of updates as a payload
 *    in the response.
 *  - `downloadFile` is a special case: it consumes [DownloadFile] payload and returns an empty-payload response.
 */
class TelegramVerticle : VertigramVerticle<TelegramVerticle.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()
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

        consumer(
            typedConfig.updatesAddress(), function = ::handleGetUpdates
        )

        for ((tgvAddress, requestType) in VertigramTypeHints.Json.requestTypeByTgvAddress) {
            if (tgvAddress in VertigramTypeHints.doNotGenerateInTgVerticleAddresses)
                continue
            consumer(
                typedConfig.callAddress(tgvAddress,
                    RequestType.Json
                ),
                requestJavaType = requestType
            ) { msg: TelegramCallable<*> ->
                tg.call(msg)
            }
        }

        for ((tgvAddress, requestType) in VertigramTypeHints.Multipart.requestTypeByTgvAddress) {
            if (tgvAddress in VertigramTypeHints.doNotGenerateInTgVerticleAddresses)
                continue
            consumer(
                typedConfig.callAddress(tgvAddress,
                    RequestType.Multipart
                ),
                requestJavaType = requestType
            ) { msg: TelegramCallable<*> ->
                tg.call(msg)
            }
        }

        consumer(typedConfig.longPollTimeoutAddress(), function = ::handleLongPollTimeout)
        consumer(typedConfig.downloadFileAddress(), function = ::handleDownloadFile)
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
            fun <T : TelegramCallable<*>> byClass(clazz: Class<T>) =
                when {
                    JsonTelegramCallable::class.java.isAssignableFrom(clazz) -> Json
                    MultipartTelegramCallable::class.java.isAssignableFrom(clazz) -> Multipart
                    else -> throw AssertionError("oops")
                }

            fun byCallable(tgc: TelegramCallable<*>) =
                when (tgc) {
                    is JsonTelegramCallable<*> -> Json
                    is MultipartTelegramCallable<*> -> Multipart
                }
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
         * Base address to listen
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

            fun <T : TelegramCallable<*>> callAddress(clazz: Class<T>, baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE) =
                callAddress(
                    VertigramTypeHints.tgvAddressByClass.getOrAssert(
                        clazz
                    ),
                    baseAddress,
                    RequestType.byClass(
                        clazz
                    )
                )

            /**
             * Consumer address for [obj]
             */
            fun <T: TelegramCallable<*>> callAddress(obj: T, baseAddress: String = TelegramAddress.TELEGRAM_VERTICLE_BASE) =
                callAddress(
                    VertigramTypeHints.tgvAddressByClass.getOrAssert(
                        obj.javaClass
                    ),
                    baseAddress,
                    RequestType.byCallable(
                        obj
                    )
                )

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
