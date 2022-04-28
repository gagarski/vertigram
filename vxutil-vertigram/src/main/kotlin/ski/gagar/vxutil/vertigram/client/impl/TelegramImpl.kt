package ski.gagar.vxutil.vertigram.client.impl

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
import io.vertx.kotlin.coroutines.await
import ski.gagar.vxutil.logger
import ski.gagar.vxutil.uncheckedCast
import ski.gagar.vxutil.vertigram.methods.JsonTgCallable
import ski.gagar.vxutil.vertigram.methods.MultipartTgCallable
import ski.gagar.vxutil.vertigram.methods.TgCallable
import ski.gagar.vxutil.vertigram.types.Wrapper
import ski.gagar.vxutil.vertigram.util.TelegramCallException
import ski.gagar.vxutil.vertigram.util.TelegramDownloadException
import ski.gagar.vxutil.vertigram.util.TypeHints
import ski.gagar.vxutil.vertigram.util.getOrAssert
import ski.gagar.vxutil.vertigram.util.json.TELEGRAM_JSON_MAPPER
import ski.gagar.vxutil.vertigram.util.multipart.TELEGRAM_JSON_MAPPER_WITH_MULTIPART
import ski.gagar.vxutil.web.bodyAsJson
import ski.gagar.vxutil.web.sendJsonAwait
import java.time.Duration

internal data class TelegramImplOptions(
    val tgBase: String = "https://api.telegram.org",
    val shortPollTimeout: Duration = Duration.ofSeconds(5),
    val longPollTimeout: Duration = Duration.ofSeconds(60),
    val pools: Pools? = null
) {
    data class Pools(
        val regular: Int?,
        val upload: Int?,
        val longPoll: Int?,
        val download: Int?
    )
}

@PublishedApi
internal class TelegramImpl(
    private val token: String,
    vertx: Vertx,
    private val proxy: ProxyOptions? = null,
    private val options: TelegramImplOptions = TelegramImplOptions()
) {
    private fun makeClientOptions(poolSize: Int?) =
        WebClientOptions().apply {
            proxy?.let {
                this.proxyOptions = proxy
            }

            poolSize?.let {
                this.maxPoolSize = poolSize
            }
        }

    private val fs by lazy {
        vertx.fileSystem()
    }

    private val regularClient = WebClient.create(vertx, makeClientOptions(options.pools?.regular))
    private val uploadClient = WebClient.create(vertx, makeClientOptions(options.pools?.upload))
    private val longPollClient = WebClient.create(vertx, makeClientOptions(options.pools?.longPoll))
    private val downloadClient = WebClient.create(vertx, makeClientOptions(options.pools?.download))

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
                    timeout(options.longPollTimeout.toMillis())
                } else {
                    timeout(options.longPollTimeout.toMillis())
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
        val resp = client(method, longPoll, upload).sendMultipartForm(form).await()
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
            TELEGRAM_JSON_MAPPER_WITH_MULTIPART.toMultipart(mpc),
            upload = true
        )

    suspend fun <T> call(type: JavaType, callable: TgCallable<T>, longPoll: Boolean = false): T =
        when(callable) {
            is JsonTgCallable<T> -> callJson(type, callable, longPoll)
            is MultipartTgCallable<T> -> callMultipart(type, callable)
        }

    suspend fun downloadFile(path: String, outputPath: String) {
        val f = fs.open(outputPath, OpenOptions().apply {
            isTruncateExisting = true
        }).await()
        val resp =
            downloadClient.getAbs("${options.tgBase}/file/bot$token/${path}").`as`(BodyCodec.pipe(f)).send().await()
        if (resp.statusCode() != 200) {
            fs.delete(outputPath).await()
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
