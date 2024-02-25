package ski.gagar.vertigram.verticles

import com.fasterxml.jackson.core.type.TypeReference
import ski.gagar.vertigram.client.DirectTelegram
import ski.gagar.vertigram.client.Telegram
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.methods.JsonTelegramCallable
import ski.gagar.vertigram.methods.MultipartTelegramCallable
import ski.gagar.vertigram.methods.TelegramCallable
import ski.gagar.vertigram.throttling.ThrottlingOptions
import ski.gagar.vertigram.throttling.ThrottlingTelegram
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.UpdateList
import ski.gagar.vertigram.util.VertigramTypeHints
import ski.gagar.vertigram.util.getOrAssert

class TelegramVerticle : VertigramVerticle<TelegramVerticle.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()
    private lateinit var tg: Telegram

    override suspend fun start() {
        val directTg = DirectTelegram(
            typedConfig.token,
            vertx,
            typedConfig.tgOptions
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
    ) = typedConfig.tgOptions.longPollTimeout

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

    data class Config(
        val token: String,
        val baseAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE,
        val tgOptions: DirectTelegram.Options = DirectTelegram.Options(),
        val throttling: ThrottlingOptions? = ThrottlingOptions()
    ) {

        internal fun callAddress(methodName: String, requestType: RequestType) =
            callAddress(
                methodName,
                baseAddress,
                requestType
            )

        fun updatesAddress() = updatesAddress(baseAddress)

        fun <T : TelegramCallable<*>> callAddress(clazz: Class<T>) =
            callAddress(
                clazz,
                baseAddress
            )

        inline fun <reified T: TelegramCallable<*>> callAddress(obj: T) =
            callAddress(
                obj,
                baseAddress
            )

        fun longPollTimeoutAddress() =
            longPollTimeoutAddress(
                baseAddress
            )

        fun downloadFileAddress() =
            downloadFileAddress(baseAddress)

        companion object {
            const val GET_UPDATES = "getUpdates"

            private fun callAddress(methodName: String,
                                    baseAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE,
                                    requestType: RequestType
            ) =
                "$baseAddress.$methodName.${requestType.postfix}"

            fun updatesAddress(baseAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE) =
                callAddress(GET_UPDATES, baseAddress, RequestType.Json)

            fun <T : TelegramCallable<*>> callAddress(clazz: Class<T>, baseAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE) =
                callAddress(
                    VertigramTypeHints.tgvAddressByClass.getOrAssert(
                        clazz
                    ),
                    baseAddress,
                    RequestType.byClass(
                        clazz
                    )
                )

            fun <T: TelegramCallable<*>> callAddress(obj: T, baseAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE) =
                callAddress(
                    VertigramTypeHints.tgvAddressByClass.getOrAssert(
                        obj.javaClass
                    ),
                    baseAddress,
                    RequestType.byCallable(
                        obj
                    )
                )

            fun longPollTimeoutAddress(baseAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE) =
                "$baseAddress.conf.longPollTimeout"

            fun downloadFileAddress(baseAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE) =
                "$baseAddress.conf.downloadFile"

        }
    }

    data class GetUpdates(val offset: Long?, val limit: Int?, val allowedUpdates: List<Update.Type>? = null)
    object GetLongPollTimeout
    data class DownloadFile(val path: String, val outputPath: String)
}
