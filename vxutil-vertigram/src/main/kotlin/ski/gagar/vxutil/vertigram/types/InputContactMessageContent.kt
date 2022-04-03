package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InputContactMessageContent.
 */
data class InputContactMessageContent(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val vcard: String? = null
)
