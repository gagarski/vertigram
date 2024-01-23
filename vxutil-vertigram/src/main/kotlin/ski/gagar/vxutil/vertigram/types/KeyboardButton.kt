package ski.gagar.vxutil.vertigram.types

data class KeyboardButton(
    val text: String,
    val requestContact: Boolean? = false,
    val requestLocation: Boolean? = false,
    val requestPoll: KeyboardButtonPollType? = null,
    val webApp: WebAppInfo? = null,
    // Since Telegram Bot Api7.0
    val requestUsers: KeyboardButtonRequestUsers? = null,
    val requestChat: KeyboardButtonRequestChat? = null,
)
