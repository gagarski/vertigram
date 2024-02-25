package ski.gagar.vertigram.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.JsonObject
import ski.gagar.vertigram.uncheckedCast


fun <T> JsonObject.mapTo(clazz: Class<T>, mapper: ObjectMapper): T =
    mapper.convertValue(this.map, clazz)

inline fun <reified T> JsonObject.mapTo(mapper: ObjectMapper): T =
    mapTo(typeReference<T>(), mapper)

fun <T> JsonObject.mapTo(type: JavaType, mapper: ObjectMapper): T =
    mapper.convertValue(this.map, type)

fun <T> JsonObject.mapTo(typeRef: TypeReference<T>, mapper: ObjectMapper): T =
    mapper.convertValue(this.map, typeRef)


fun <T> T?.toJsonObject(mapper: ObjectMapper) = this?.let {
    JsonObject(mapper.convertValue(this, Map::class.java).uncheckedCast<Map<String, Any?>>())
}

inline fun <reified T> typeReference(): TypeReference<T> = object : TypeReference<T>() {}