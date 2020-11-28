package ski.gagar.vertigram.client.impl

import com.fasterxml.jackson.databind.JavaType
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.core.net.ProxyOptions
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.ext.web.multipart.MultipartForm
import io.vertx.kotlin.core.file.deleteAwait
import io.vertx.kotlin.core.file.openAwait
import io.vertx.kotlin.ext.web.client.sendAwait
import io.vertx.kotlin.ext.web.client.sendMultipartFormAwait
import ski.gagar.vxutil.logger
import ski.gagar.vertigram.entities.Wrapper
import ski.gagar.vertigram.entities.requests.JsonTgCallable
import ski.gagar.vertigram.entities.requests.MultipartTgCallable
import ski.gagar.vertigram.entities.requests.TgCallable
import ski.gagar.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vertigram.util.*
import ski.gagar.vertigram.util.TypeHints
import ski.gagar.vertigram.util.getOrAssert
import ski.gagar.vxutil.bodyAsJson
import ski.gagar.vxutil.sendJsonAwait
import ski.gagar.vxutil.uncheckedCast

internal data class TelegramImplOptions(
    val tgBase: String = "https://api.telegram.org",
    val shortPollTimeout: Long = 5000L,
    val longPollTimeout: Long = 60000L
)

@PublishedApi
internal class TelegramImpl(
    private val token: String,
    vertx: Vertx,
    proxy: ProxyOptions? = null,
    private val options: TelegramImplOptions = TelegramImplOptions()
) {
    private val clientOptions: WebClientOptions = WebClientOptions().apply {
        proxy?.let {
            this.proxyOptions = proxy
            this.maxPoolSize
        }
    }

    private val fs by lazy {
        vertx.fileSystem()
    }

    private val regularClient = WebClient.create(vertx, clientOptions)
    private val uploadClient = WebClient.create(vertx, clientOptions)
    private val longPollClient = WebClient.create(vertx, clientOptions)
    private val downloadClient = WebClient.create(vertx, clientOptions)

    @PublishedApi
    internal val mapper = TELEGRAM_JSON_MAPPER

    private fun client(longPoll: Boolean = false, upload: Boolean = false): WebClient =
        when {
            upload -> uploadClient
            longPoll -> longPollClient
            else -> regularClient
        }

    private fun client(method: String, longPoll: Boolean = false, upload: Boolean = false) =
        client(longPoll, upload).postAbs("${options.tgBase}/bot$token/$method")
            .putHeader("${HttpHeaderNames.ACCEPT}", "${HttpHeaderValues.APPLICATION_JSON}").apply {
                if (longPoll) {
                    timeout(options.longPollTimeout)
                } else {
                    timeout(options.longPollTimeout)
                }
            }

    @Deprecated("Use more high-level methods")
    @PublishedApi
    internal suspend fun <Req> callForObject(
        method: String,
        type: JavaType,
        obj: Req? = null,
        longPoll: Boolean = false,
        upload: Boolean = false
    ): Pair<Int, Any?> {
        logger.trace("Calling $method with $obj")
        val resp = client(method, longPoll, upload).sendJsonAwait(obj, mapper)
        return resp.statusCode() to resp.bodyAsJson<Any>(type, mapper).also {
            logger.trace("Received response $it")
        }
    }

    @Deprecated("Use more high-level methods")
    internal suspend fun callForObjectMultipart(
        method: String,
        type: JavaType,
        form: MultipartForm,
        longPoll: Boolean = false,
        upload: Boolean = true
    ): Pair<Int, Any?> {
        logger.trace("Calling $method with $form (form/multipart)")
        val resp = client(method, longPoll, upload).sendMultipartFormAwait(form)
        return resp.statusCode() to resp.bodyAsJson<Any>(type, mapper).also {
            logger.trace("Received response $it")
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods for any API-method
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Suppress("DEPRECATION")
    private suspend fun <Req, Resp> callForWrapper(
        respType: JavaType,
        method: String,
        obj: Req? = null,
        longPoll: Boolean = false,
        upload: Boolean = false
    ): Pair<Int, Wrapper<Resp>> =
        callForObject(
            method,
            mapper.typeFactory.constructParametricType(Wrapper::class.java, respType),
            obj,
            longPoll,
            upload
        ).uncheckedCast()

    @Suppress("DEPRECATION")
    private suspend fun <Resp> callForWrapperMultipart(
        respType: JavaType,
        method: String,
        form: MultipartForm,
        longPoll: Boolean = false,
        upload: Boolean = true
    ): Pair<Int, Wrapper<Resp>> =
        callForObjectMultipart(
            method,
            mapper.typeFactory.constructParametricType(Wrapper::class.java, respType),
            form,
            longPoll,
            upload
        ).uncheckedCast()

    private suspend inline fun <Req, Resp> call(
        respType: JavaType,
        method: String,
        obj: Req? = null,
        longPoll: Boolean = false,
        upload: Boolean = false
    ): Resp {
        val (status, wrapper) = callForWrapper<Req, Resp>(respType, method, obj, longPoll, upload)

        if (status != 200 || !wrapper.ok)
            throw TelegramCallException(status, wrapper.ok, wrapper.description)

        return wrapper.result!!
    }

    private suspend fun <Resp> callMultipart(
        respType: JavaType,
        method: String,
        form: MultipartForm,
        longPoll: Boolean = false,
        upload: Boolean = true
    ): Resp {
        val (status, wrapper) = callForWrapperMultipart<Resp>(respType, method, form, longPoll, upload)

        if (status != 200 || !wrapper.ok)
            throw TelegramCallException(status, wrapper.ok, wrapper.description)

        return wrapper.result!!
    }

    @PublishedApi
    internal suspend fun <T> callJson(type: JavaType, jc: JsonTgCallable<T>, longPoll: Boolean = false): T =
        call(type, TypeHints.methodNames.getOrAssert(jc.javaClass), jc, longPoll = longPoll)

    private suspend fun <T> callMultipart(type: JavaType, mpc: MultipartTgCallable<T>): T =
        callMultipart(
            type,
            TypeHints.methodNames.getOrAssert(mpc.javaClass),
            mpc.serializeToMultipart(mapper),
            upload = true
        )

    suspend fun <T> call(type: JavaType, callable: TgCallable<T>, longPoll: Boolean = false): T =
        when(callable) {
            is JsonTgCallable<T> -> callJson(type, callable, longPoll)
            is MultipartTgCallable<T> -> callMultipart(type, callable)
        }

    suspend fun downloadFile(path: String, outputPath: String): Unit {
        val f = fs.openAwait(outputPath, OpenOptions().apply {
            isTruncateExisting = true
        })
        val resp = downloadClient.getAbs("${options.tgBase}/file/bot$token/${path}").`as`(BodyCodec.pipe(f)).sendAwait()
        if (resp.statusCode() != 200) {
            fs.deleteAwait(outputPath)
            // TODO try to get a response from file and parse it
            throw TelegramDownloadException(resp.statusCode())
        }
    }

    fun close() {
        regularClient.close()
        longPollClient.close()
        uploadClient.close()
        downloadClient.close()
    }

}
