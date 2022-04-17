package ski.gagar.vxutil.vertigram.types

data class Wrapper<T>(
    val ok: Boolean,
    val result: T?,
    val description: String? = null
)
