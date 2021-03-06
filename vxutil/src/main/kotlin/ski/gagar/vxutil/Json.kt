package ski.gagar.vxutil

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.DecodeException
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.client.HttpResponse

fun <T> decodeValue(str: String, type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): T {
    try {
        return mapper.readValue(str, type)
    } catch (var3: Exception) {
        throw DecodeException("Failed to decode: " + var3.message, var3)
    }

}

@PublishedApi
internal fun <T> jsonDecoder(type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): (Buffer) -> T {
    return { buff -> decodeValue(buff.toString(), type, mapper) }
}

fun <R> HttpResponse<Buffer>.bodyAsJson(type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): R? {
    val b = bodyAsBuffer()
    return if (b != null) jsonDecoder<R>(type, mapper)(b) else null
}

fun jsonObjectMapFrom(obj: Any?, mapper: ObjectMapper = DatabindCodec.mapper()) =
    obj?.let {
        JsonObject(mapper.convertValue(it, Map::class.java).uncheckedCast<Map<String, Any?>>())
    }

fun <T> JsonObject.mapTo(clazz: Class<T>, mapper: ObjectMapper = DatabindCodec.mapper()): T =
    mapper.convertValue(this.map, clazz)

inline fun <reified T> JsonObject.mapTo(mapper: ObjectMapper = DatabindCodec.mapper()): T =
    mapper.convertValue(this.map, mapper.typeFactory.constructType(T::class.java))

fun <T> JsonObject.mapTo(type: JavaType, mapper: ObjectMapper = DatabindCodec.mapper()): T =
    mapper.convertValue(this.map, type)