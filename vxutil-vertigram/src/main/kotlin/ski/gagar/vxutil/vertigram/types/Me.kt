package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type User.
 *
 * This class has properties returned from getMe Telegram method.
 * @see ski.gagar.vxutil.vertigram.types.User
 */
data class Me(
    val id: Long,
    @get:JvmName("getIsBot")
    val isBot: Boolean,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val languageCode: String? = null,
    val canJoinGroups: Boolean = false,
    val canReadAllGroupMessages: Boolean = false,
    val supportsInlineQueries: Boolean = false
)
