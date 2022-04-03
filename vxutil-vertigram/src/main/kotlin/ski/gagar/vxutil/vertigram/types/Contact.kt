package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Contact.
 */
data class Contact(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null
)
