package ski.gagar.vxutil.vertigram.types

/**
 * Common wrapper for Telegram response.
 */
data class Wrapper<T>(
    val ok: Boolean,
    val result: T?,
    val description: String? = null
)
