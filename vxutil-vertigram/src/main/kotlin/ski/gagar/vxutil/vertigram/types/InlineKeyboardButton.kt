package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineKeyboardButton.
 */
data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val loginUrl: LoginUrl? = null,
    val callbackData: String? = null,
    val switchInlineQuery: String? = null,
    val switchInlineQueryCurrentChat: String? = null,
    val callbackGame: CallbackGame? = null,
    val pay: Boolean = false
)
