package ski.gagar.vxutil.vertigram.entities

data class Contact(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null
)
