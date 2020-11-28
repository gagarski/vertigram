package ski.gagar.vertigram.entities


data class LoginUrl(
    val url: String,
    val forwardText: String? = null,
    val botUsername: String? = null,
    val requestWriteAccess: Boolean = false // Optional by specification, sent as false by default
)