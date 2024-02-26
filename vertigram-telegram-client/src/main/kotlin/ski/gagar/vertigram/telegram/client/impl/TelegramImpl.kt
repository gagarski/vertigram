package ski.gagar.vertigram.telegram.client.impl

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.file.OpenOptions
import io.vertx.core.json.DecodeException
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.core.net.ProxyOptions
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import io.vertx.ext.web.codec.BodyCodec
import io.vertx.kotlin.coroutines.coAwait
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.telegram.methods.JsonTelegramCallable
import ski.gagar.vertigram.telegram.methods.MultipartTelegramCallable
import ski.gagar.vertigram.telegram.methods.TelegramCallable
import ski.gagar.vertigram.toMap
import ski.gagar.vertigram.telegram.types.Wrapper
import ski.gagar.vertigram.uncheckedCast
import ski.gagar.vertigram.telegram.exceptions.TelegramCallException
import ski.gagar.vertigram.telegram.exceptions.TelegramDownloadException
import ski.gagar.vertigram.util.VertigramTypeHints
import ski.gagar.vertigram.util.getOrAssert
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER
import ski.gagar.vertigram.util.multipart.telegramJsonMapperWithMultipart
import ski.gagar.vertigram.web.multipart.FieldPart
import ski.gagar.vertigram.web.multipart.MultipartForm
import ski.gagar.vertigram.web.multipart.sendMultipartForm
import java.time.Duration

/**
 * Options for [TelegramImpl]
 */
internal data class TelegramImplOptions(
    /**
     * Base telegram URL
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
     * HTTP client pool configs
     */
    val pools: Pools? = null
) {
    /**
     * HTTP client pool configs
     */
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

/**
 * Internal low-level Telegram implementation
 */
internal class TelegramImpl(
    /**
     * Auth token
     */
    private val token: String,
    /**
     * [Vertx] instance
     */
    vertx: Vertx,
    /**
     * Proxy options
     */
    private val proxy: ProxyOptions? = null,
    /**
     * Options
     */
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

    internal val mapper = TELEGRAM_JSON_MAPPER
    private val mapperMp = telegramJsonMapperWithMultipart(mapper, vertx)

    private fun <T> decodeValue(str: String, type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): T {
        try {
            return mapper.readValue(str, type)
        } catch (var3: Exception) {
            throw DecodeException("Failed to decode: " + var3.message, var3)
        }

    }

    private fun <T> jsonDecoder(type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): (Buffer) -> T {
        return { buff -> decodeValue(buff.toString(), type, mapper) }
    }


    private fun jsonObjectMapFrom(obj: Any?) =
        obj?.let {
            JsonObject(mapper.convertValue(it, Map::class.java).uncheckedCast<Map<String, Any?>>())
        }

    private fun <T, U> HttpRequest<T>.sendJsonGeneric(obj: U) =
        sendJsonObject(
            jsonObjectMapFrom(
                obj
            )
        )

    private fun <R> HttpResponse<Buffer>.jsonBody(type: JavaType): R? {
        val b = bodyAsBuffer()
        return if (b != null) jsonDecoder<R>(type, mapper)(b) else null
    }


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

    private suspend fun <Req> callForObject(
        method: String,
        type: JavaType,
        obj: Req? = null,
        longPoll: Boolean = false
    ): Pair<HttpResponse<*>, Any?> {
        logger.lazy.trace { "Calling $method with $obj" }
        val resp = client(method, longPoll, false).sendJsonGeneric(obj).coAwait()
        return resp to resp.jsonBody<Any>(type).also {
            logger.lazy.trace { "Received response $it" }
        }
    }

    private suspend fun callForObjectMultipart(
        method: String,
        type: JavaType,
        form: MultipartForm,
        longPoll: Boolean = false
    ): Pair<HttpResponse<*>, Any?> {
        logger.lazy.trace { "Calling $method with $form (form/multipart)" }
        val resp = client(method, longPoll, !form.parts.all { it is FieldPart }).sendMultipartForm(form)
        return resp to resp.jsonBody<Any>(type).also {
            logger.lazy.trace { "Received response $it" }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods for any API-method
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private suspend fun <Req, Resp> callForWrapper(
        respType: JavaType,
        method: String,
        obj: Req? = null,
        longPoll: Boolean = false
    ): Pair<HttpResponse<*>, Wrapper<Resp>> =
        callForObject(
            method,
            mapper.typeFactory.constructParametricType(Wrapper::class.java, respType),
            obj,
            longPoll
        ).uncheckedCast()

    private suspend fun <Resp> callForWrapperMultipart(
        respType: JavaType,
        method: String,
        form: MultipartForm,
        longPoll: Boolean = false
    ): Pair<HttpResponse<*>, Wrapper<Resp>> =
        callForObjectMultipart(
            method,
            mapper.typeFactory.constructParametricType(Wrapper::class.java, respType),
            form,
            longPoll
        ).uncheckedCast()

    private suspend inline fun <Req: JsonTelegramCallable<*>, Resp> call(
        respType: JavaType,
        method: String,
        obj: Req,
        longPoll: Boolean = false
    ): Resp {
        val (response, wrapper) = callForWrapper<Req, Resp>(respType, method, obj, longPoll)

        if (response.statusCode() != 200 || !wrapper.ok)
            throw TelegramCallException.create(response.statusCode(), wrapper.ok, wrapper.description, obj,
                response.headers().toMap())

        return wrapper.result!!
    }

    private suspend fun <Req: MultipartTelegramCallable<*>, Resp> callMultipart(
        respType: JavaType,
        method: String,
        mpc: Req,
        longPoll: Boolean = false
    ): Resp {
        val (response, wrapper) = callForWrapperMultipart<Resp>(
            respType,
            method,
            mapperMp.toMultipart(mpc),
            longPoll)

        if (response.statusCode() != 200 || !wrapper.ok)
            throw TelegramCallException.create(response.statusCode(), wrapper.ok, wrapper.description, mpc,
                response.headers().toMap())

        return wrapper.result!!
    }

    private suspend fun <T> callJson(type: JavaType, jc: JsonTelegramCallable<T>, longPoll: Boolean = false): T =
        call(type, VertigramTypeHints.methodNameByClass.getOrAssert(jc.javaClass), jc, longPoll = longPoll)

    private suspend fun <T> callMultipart(type: JavaType, mpc: MultipartTelegramCallable<T>): T =
        callMultipart(
            type,
            VertigramTypeHints.methodNameByClass.getOrAssert(mpc.javaClass),
            mpc
        )

    /**
     * Entry point for calling methods
     */
    suspend fun <T> call(type: JavaType, callable: TelegramCallable<T>, longPoll: Boolean = false): T =
        when (callable) {
            is JsonTelegramCallable<T> -> callJson(type, callable, longPoll)
            is MultipartTelegramCallable<T> -> callMultipart(type, callable)
        }

    /**
     * Entry point for downloading files
     */
    suspend fun downloadFile(path: String, outputPath: String) {
        val f = fs.open(outputPath, OpenOptions().apply {
            isTruncateExisting = true
        }).coAwait()
        val resp =
            downloadClient.getAbs("${options.tgBase}/file/bot$token/${path}").`as`(BodyCodec.pipe(f)).send().coAwait()
        if (resp.statusCode() != 200) {
            fs.delete(outputPath).coAwait()
            // TODO try to get a response from file and parse it
            throw TelegramDownloadException.create(resp.statusCode(), path)
        }
    }

    /**
     * Close
     */
    fun close() {
        regularClient.close()
        longPollClient.close()
        uploadClient.close()
        downloadClient.close()
    }

}
