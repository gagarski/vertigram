package ski.gagar.vertigram.util.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.JsonObject
import ski.gagar.vertigram.util.internal.uncheckedCast

/**
 * Map [this] to [T] using [mapper]
 */
inline fun <reified T> JsonObject.mapTo(mapper: ObjectMapper): T =
    mapTo(mapper.constructType(typeReference<T>().type), mapper)

/**
 * Map [this] to [clazz] using [mapper]
 */

fun <T> JsonObject.mapTo(clazz: Class<T>, mapper: ObjectMapper): T =
    mapper.convertValue(this.map, clazz)

/**
 * Map [this] to [type] using [mapper]
 */
fun <T> JsonObject.mapTo(type: JavaType, mapper: ObjectMapper): T =
    mapper.convertValue(this.map, type)


/**
 * Map [this] to [JsonObject] using [mapper]
 */
fun <T> T?.toJsonObject(mapper: ObjectMapper) = this?.let {
    JsonObject(mapper.convertValue(this, Map::class.java).uncheckedCast<Map<String, Any?>>())
}

/**
 * Convenience function to create an anonymous [TypeReference] object.
 */
inline fun <reified T> typeReference(): TypeReference<T> = object : TypeReference<T>() {}