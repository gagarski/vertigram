package ski.gagar.vxutil.web

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.kotlin.coroutines.awaitResult
import ski.gagar.vxutil.jsonDecoder
import ski.gagar.vxutil.jsonObjectMapFrom

suspend fun <T, U> HttpRequest<T>.sendJsonAwait(obj: U, mapper: ObjectMapper) =
    awaitResult<HttpResponse<T>> { sendJsonObject(
        jsonObjectMapFrom(
            obj,
            mapper
        ), it) }

fun <R> HttpResponse<Buffer>.bodyAsJson(type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): R? {
    val b = bodyAsBuffer()
    return if (b != null) jsonDecoder<R>(type, mapper)(b) else null
}
