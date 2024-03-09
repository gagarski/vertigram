package ski.gagar.vertigram.dokka.tool

data class Version(val parts: List<Int>, val majorParts: Int = 2) : Comparable<Version> {
    init {
        require(parts.size >= majorParts)
    }

    val major by lazy {
        Version(parts.take(majorParts), majorParts)
    }

    override fun compareTo(other: Version): Int {
        val result = parts
            .zip(other.parts)
            .map { (this_, other_) ->
                 this_.compareTo(other_)
            }
            .firstOrNull { it != 0 }
            ?: 0

        if (result != 0) {
            return result
        }

        return parts.size.compareTo(other.parts.size)
    }

    override fun toString(): String = parts.joinToString(".")


}

fun String.toVersion(majorParts: Int = 2) = Version(split(".").map { it.toInt() }, majorParts)

fun String.toVersionOrNull(majorParts: Int = 2) = try {
    toVersion(majorParts)
} catch (ex: IllegalArgumentException) {
    null
}