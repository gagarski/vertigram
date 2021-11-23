package ski.gagar.vxutil.vertigram.entities

data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val loginUrl: LoginUrl? = null,
    val callbackData: String? = null,
    val switchInlineQuery: String? = null,
    val switchInlineQueryCurrentChat: String? = null,
    val callbackGame: Game? = null,
    val pay: Boolean? = null
)
