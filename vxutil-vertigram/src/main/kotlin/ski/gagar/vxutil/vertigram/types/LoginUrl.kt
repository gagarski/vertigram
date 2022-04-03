package ski.gagar.vxutil.vertigram.types

/**
 * Telegram Type LoginUrl
 */
data class LoginUrl(
    val url: String,
    val forwardText: String? = null,
    val botUsername: String? = null,
    val requestWriteAccess: Boolean = false
)
