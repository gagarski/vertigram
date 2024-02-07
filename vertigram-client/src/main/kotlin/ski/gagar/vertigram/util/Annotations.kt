package ski.gagar.vertigram.util

internal annotation class TgMethodName(val name: String)

const val DEFAULT_ADDRESS = "DEFAULT"

internal annotation class TgVerticleGenerate(
    val generate: Boolean = true,
    val address: String = DEFAULT_ADDRESS
)
