package ski.gagar.vxutil.vertigram.types

data class User(
    val id: Long,
    @get:JvmName("getIsBot")
    val isBot: Boolean = false,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null
)
