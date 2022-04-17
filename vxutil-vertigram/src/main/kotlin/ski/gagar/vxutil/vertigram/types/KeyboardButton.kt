package ski.gagar.vxutil.vertigram.types

data class KeyboardButton(
    val text: String,
    val requestContact: Boolean? = false,
    val requestLocation: Boolean? = false,
    val requestPoll: KeyboardButtonPollType? = null,
    val webApp: WebAppInfo? = null
)
