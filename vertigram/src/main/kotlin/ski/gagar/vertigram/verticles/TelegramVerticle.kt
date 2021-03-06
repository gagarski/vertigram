package ski.gagar.vertigram.verticles

import io.vertx.kotlin.coroutines.CoroutineVerticle
import ski.gagar.vertigram.client.DirectTelegram
import ski.gagar.vertigram.entities.requests.GetUpdates
import ski.gagar.vertigram.entities.requests.JsonTgCallable
import ski.gagar.vertigram.entities.requests.MultipartTgCallable
import ski.gagar.vertigram.entities.requests.TgCallable
import ski.gagar.vertigram.util.TypeHints
import ski.gagar.vertigram.util.getOrAssert
import ski.gagar.vxutil.mapTo
import ski.gagar.vxutil.suspendJsonConsumer

@Suppress("DEPRECATION")
private typealias RawGetUpdates = GetUpdates

class TelegramVerticle : CoroutineVerticle() {
    private val typedConfig by lazy {
        config.mapTo<Config>()
    }
    private lateinit var tg: DirectTelegram

    override suspend fun start() {
        tg = DirectTelegram(
            typedConfig.token,
            vertx,
            typedConfig.tgOptions
        )

        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
        suspendJsonConsumer(
            typedConfig.callAddress(RawGetUpdates::class.java), function = ::handleGetUpdates)

        for ((methodName, requestType) in TypeHints.Json.requestTypesByMethodName) {
            if (methodName in TypeHints.doNotGenerateInTgVerticleMethodNames)
                continue
            val responseType = TypeHints.Json.returnTypesByMethodName.getOrAssert(methodName)
            suspendJsonConsumer(
                typedConfig.callAddress(methodName,
                    RequestType.Json
                ),
                requestJavaType = requestType
            ) { msg: TgCallable<*> ->
                tg.call(responseType, msg)
            }
        }

        for ((methodName, requestType) in TypeHints.Multipart.requestTypesByMethodName) {
            if (methodName in TypeHints.doNotGenerateInTgVerticleMethodNames)
                continue
            val responseType = TypeHints.Multipart.returnTypesByMethodName.getOrAssert(methodName)
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

    private suspend fun handleGetUpdates(msg: GetUpdates) = tg.getUpdates(limit = msg.limit, offset = msg.offset)

    private fun handleLongPollTimeout(msg: GetLongPollTimeout) = typedConfig.tgOptions.longPollTimeout

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
        val tgOptions: DirectTelegram.Options = DirectTelegram.Options()
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
                    TypeHints.methodNames.getOrAssert(
                        clazz
                    ),
                    baseAddress,
                    RequestType.byClass(
                        clazz
                    )
                )

            fun <T: TgCallable<*>> callAddress(obj: T, baseAddress: String = DEFAULT_BASE_ADDRESS) =
                callAddress(
                    TypeHints.methodNames.getOrAssert(
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

    data class GetUpdates(val offset: Long?, val limit: Long?)
    object GetLongPollTimeout
    data class DownloadFile(val path: String, val outputPath: String)
}
