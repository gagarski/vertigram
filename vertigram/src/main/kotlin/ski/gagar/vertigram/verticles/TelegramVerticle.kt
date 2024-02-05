package ski.gagar.vertigram.verticles

import ski.gagar.vertigram.client.DirectTelegram
import ski.gagar.vertigram.client.Telegram
import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.jackson.suspendJsonConsumer
import ski.gagar.vertigram.methods.JsonTgCallable
import ski.gagar.vertigram.methods.MultipartTgCallable
import ski.gagar.vertigram.methods.TgCallable
import ski.gagar.vertigram.throttling.ThrottlingOptions
import ski.gagar.vertigram.throttling.ThrottlingTelegram
import ski.gagar.vertigram.types.UpdateList
import ski.gagar.vertigram.types.UpdateType
import ski.gagar.vertigram.use
import ski.gagar.vertigram.util.VertigramTypeHints
import ski.gagar.vertigram.util.getOrAssert

@Suppress("DEPRECATION")
private typealias RawGetUpdates = ski.gagar.vertigram.methods.GetUpdates

class TelegramVerticle : ErrorLoggingCoroutineVerticle() {
    private val typedConfig by lazy {
        config.mapTo<Config>()
    }
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

        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
        suspendJsonConsumer(
            typedConfig.callAddress(RawGetUpdates::class.java), function = ::handleGetUpdates)

        for ((methodName, requestType) in VertigramTypeHints.Json.requestTypesByMethodName) {
            if (methodName in VertigramTypeHints.doNotGenerateInTgVerticleMethodNames)
                continue
            val responseType = VertigramTypeHints.Json.returnTypesByMethodName.getOrAssert(methodName)
            suspendJsonConsumer(
                typedConfig.callAddress(methodName,
                    RequestType.Json
                ),
                requestJavaType = requestType
            ) { msg: TgCallable<*> ->
                tg.call(responseType, msg)
            }
        }

        for ((methodName, requestType) in VertigramTypeHints.Multipart.requestTypesByMethodName) {
            if (methodName in VertigramTypeHints.doNotGenerateInTgVerticleMethodNames)
                continue
            val responseType = VertigramTypeHints.Multipart.returnTypesByMethodName.getOrAssert(methodName)
            suspendJsonConsumer(
                typedConfig.callAddress(methodName,
                    RequestType.Multipart
                ),
                requestJavaType = requestType
            ) { msg: TgCallable<*> ->
                tg.call(responseType, msg)
            }
        }

        suspendJsonConsumer(typedConfig.longPollTimeoutAddress(), function = ::handleLongPollTimeout)
        suspendJsonConsumer(typedConfig.downloadFileAddress(), function = ::handleDownloadFile)
    }

    override suspend fun stop() {
        val tg = this.tg
        if (tg is AutoCloseable) {
            tg.close()
        }
    }

    private suspend fun handleGetUpdates(msg: GetUpdates) =
        UpdateList(tg.getUpdates(limit = msg.limit, offset = msg.offset, allowedUpdates = msg.allowedUpdates))

    private fun handleLongPollTimeout(msg: GetLongPollTimeout) = typedConfig.tgOptions.longPollTimeout.also { use(msg) }

    private suspend fun handleDownloadFile(msg: DownloadFile) = tg.downloadFile(msg.path, msg.outputPath)

    enum class RequestType(val postfix: String) {
        Json("json"),
        Multipart("multipart");

        companion object {
            fun <T : TgCallable<*>> byClass(clazz: Class<T>) =
                when {
                    JsonTgCallable::class.java.isAssignableFrom(clazz) -> Json
                    MultipartTgCallable::class.java.isAssignableFrom(clazz) -> Multipart
                    else -> throw AssertionError("oops")
                }

            fun byCallable(tgc: TgCallable<*>) =
                when (tgc) {
                    is JsonTgCallable<*> -> Json
                    is MultipartTgCallable<*> -> Multipart
                }
        }
    }

    data class Config(
        val token: String,
        val baseAddress: String = DEFAULT_BASE_ADDRESS,
        val tgOptions: DirectTelegram.Options = DirectTelegram.Options(),
        val throttling: ThrottlingOptions? = ThrottlingOptions()
    ) {

        internal fun callAddress(methodName: String, requestType: RequestType) =
            callAddress(
                methodName,
                baseAddress,
                requestType
            )

        fun <T : TgCallable<*>> callAddress(clazz: Class<T>) =
            callAddress(
                clazz,
                baseAddress
            )

        inline fun <reified T: TgCallable<*>> callAddress(obj: T) =
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
            const val DEFAULT_BASE_ADDRESS = "ski.gagar.vertigram.telegram"

            private fun callAddress(methodName: String,
                                    baseAddress: String = DEFAULT_BASE_ADDRESS,
                                    requestType: RequestType
            ) =
                "$baseAddress.$methodName.${requestType.postfix}"

            fun <T : TgCallable<*>> callAddress(clazz: Class<T>, baseAddress: String = DEFAULT_BASE_ADDRESS) =
                callAddress(
                    VertigramTypeHints.methodNames.getOrAssert(
                        clazz
                    ),
                    baseAddress,
                    RequestType.byClass(
                        clazz
                    )
                )

            fun <T: TgCallable<*>> callAddress(obj: T, baseAddress: String = DEFAULT_BASE_ADDRESS) =
                callAddress(
                    VertigramTypeHints.methodNames.getOrAssert(
                        obj.javaClass
                    ),
                    baseAddress,
                    RequestType.byCallable(
                        obj
                    )
                )

            fun longPollTimeoutAddress(baseAddress: String = DEFAULT_BASE_ADDRESS) =
                "$baseAddress.conf.longPollTimeout"

            fun downloadFileAddress(baseAddress: String = DEFAULT_BASE_ADDRESS) =
                "$baseAddress.conf.downloadFile"

        }
    }

    data class GetUpdates(val offset: Long?, val limit: Int?, val allowedUpdates: List<UpdateType>? = null)
    object GetLongPollTimeout
    data class DownloadFile(val path: String, val outputPath: String)
}
