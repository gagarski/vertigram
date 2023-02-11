package ski.gagar.vxutil.vertigram.types

data class KeyboardButtonRequestUser(
    val requestId: Long,
    val userIsBot: Boolean? = false,
    val userIsPremium: Boolean? = false
)
