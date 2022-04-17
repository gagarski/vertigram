package ski.gagar.vxutil.vertigram.types

data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val loginUrl: LoginUrl? = null,
    val callbackData: String? = null,
    val webApp: WebAppInfo? = null,
    val switchInlineQuery: String? = null,
    val switchInlineQueryCurrentChat: String? = null,
    val callbackGame: CallbackGame? = null,
    val pay: Boolean = false
)
