package ski.gagar.vxutil.vertigram.types

data class Me(
    val id: Long,
    @get:JvmName("getIsBot")
    val isBot: Boolean = false,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null,
    val canJoinGroups: Boolean = false,
    val canReadAllGroupMessages: Boolean = false,
    val supportsInlineQueries: Boolean = false
)
