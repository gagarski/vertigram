package ski.gagar.vertigram.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.JsonObject
import ski.gagar.vertigram.uncheckedCast

inline fun <reified T> JsonObject.mapTo(mapper: ObjectMapper): T =
    mapTo(mapper.constructType(typeReference<T>().type), mapper)

fun <T> JsonObject.mapTo(clazz: Class<T>, mapper: ObjectMapper): T =
    mapper.convertValue(this.map, clazz)

fun <T> JsonObject.mapTo(type: JavaType, mapper: ObjectMapper): T =
    mapper.convertValue(this.map, type)


fun <T> T?.toJsonObject(mapper: ObjectMapper) = this?.let {
    JsonObject(mapper.convertValue(this, Map::class.java).uncheckedCast<Map<String, Any?>>())
}

inline fun <reified T> typeReference(): TypeReference<T> = object : TypeReference<T>() {}