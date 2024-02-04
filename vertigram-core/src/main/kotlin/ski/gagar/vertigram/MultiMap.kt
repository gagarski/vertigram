package ski.gagar.vertigram

import io.vertx.core.MultiMap

fun MultiMap.toMap(): Map<String, List<String>> {
    val map = mutableMapOf<String, MutableList<String>>()

    for ((k, v) in this) {
        map[k] = (map[k] ?: mutableListOf()).apply {
            add(v)
        }
    }

    return map
}

fun Map<String, List<String>>.toMultiMap(constructor: () -> MultiMap): MultiMap {
    val multiMap = constructor()

    for ((k, v) in this) {
        multiMap.add(k, v)
    }

    return multiMap
}
