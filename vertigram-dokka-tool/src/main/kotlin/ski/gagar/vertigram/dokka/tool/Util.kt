package ski.gagar.vertigram.dokka.tool

import java.io.File

fun String.withTrailingSeparator(): String {
    val sep = File.separator

    if (endsWith(sep)) {
        return this
    }

    return "$this$sep"
}