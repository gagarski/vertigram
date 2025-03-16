package ski.gagar.vertigram.dokka.tool

import java.nio.file.Path


fun main() {
    println(Path.of("a/b/c").toString())
    println(Path.of("a/b/c/").toString())
}