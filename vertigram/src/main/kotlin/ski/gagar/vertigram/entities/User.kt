package ski.gagar.vertigram.entities

data class User(
    val id: Long,
    @get:JvmName("getIsBot")
    val isBot: Boolean,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null
)