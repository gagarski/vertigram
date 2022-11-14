package ski.gagar.vxutil.web

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import ski.gagar.vxutil.jackson.jsonDecoder
import ski.gagar.vxutil.jackson.jsonObjectMapFrom

fun <T, U> HttpRequest<T>.sendJson(obj: U, mapper: ObjectMapper) =
    sendJsonObject(
        jsonObjectMapFrom(
            obj,
            mapper
        )
    )

fun <R> HttpResponse<Buffer>.jsonBody(type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): R? {
    val b = bodyAsBuffer()
    return if (b != null) jsonDecoder<R>(type, mapper)(b) else null
}
