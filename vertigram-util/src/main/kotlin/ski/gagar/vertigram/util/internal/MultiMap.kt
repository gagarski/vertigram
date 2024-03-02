package ski.gagar.vertigram.util.internal

import io.vertx.core.MultiMap

/**
 * Convert [MultiMap] to [Map] with list of values of original [MultiMap] converted to a lsit in the result [Map]
 */
fun MultiMap.toMap(): Map<String, List<String>> {
    val map = mutableMapOf<String, MutableList<String>>()

    for ((k, v) in this) {
        map[k] = (map[k] ?: mutableListOf()).apply {
            add(v)
        }
    }

    return map
}

/**
 * Convert [Map] with list values to [MultiMap]
 */
fun Map<String, List<String>>.toMultiMap(constructor: () -> MultiMap): MultiMap {
    val multiMap = constructor()

    for ((k, v) in this) {
        multiMap.add(k, v)
    }

    return multiMap
}
