package ski.gagar.vxutil.vertigram.entities

data class Wrapper<T>(
    val ok: Boolean,
    val result: T?,
    val description: String? = null
)
