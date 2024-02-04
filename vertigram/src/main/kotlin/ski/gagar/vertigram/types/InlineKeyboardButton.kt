package ski.gagar.vertigram.types

data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val loginUrl: LoginUrl? = null,
    val callbackData: String? = null,
    val webApp: WebAppInfo? = null,
    val switchInlineQuery: String? = null,
    val switchInlineQueryCurrentChat: String? = null,
    val callbackGame: CallbackGame? = null,
    val pay: Boolean = false,
    // Since Telegram Bot API 6.7
    val switchInlineQueryChosenChat: SwitchInlineQueryChosenChat? = null
)
