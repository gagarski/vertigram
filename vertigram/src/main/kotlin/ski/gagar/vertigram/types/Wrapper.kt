package ski.gagar.vertigram.types

data class Wrapper<T>(
    val ok: Boolean,
    val result: T?,
    val description: String? = null
)
