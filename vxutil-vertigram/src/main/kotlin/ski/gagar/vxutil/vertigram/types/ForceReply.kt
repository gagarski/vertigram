package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ForceReply.
 */
data class ForceReply(
    val forceReply: Boolean = false,
    val inputFieldPlaceholder: String? = null,
    val selective: Boolean = false
) : ReplyMarkup()
