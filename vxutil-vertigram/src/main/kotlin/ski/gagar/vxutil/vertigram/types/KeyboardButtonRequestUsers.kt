package ski.gagar.vxutil.vertigram.types

data class KeyboardButtonRequestUsers(
    val requestId: Long,
    val userIsBot: Boolean? = false,
    val userIsPremium: Boolean? = false,
    val maxQuantity: Long = 1
)
