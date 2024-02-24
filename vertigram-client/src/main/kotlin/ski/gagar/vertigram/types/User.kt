package ski.gagar.vertigram.types

/**
 * A common interface for [User] and [User.Me] types.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
interface IUser {
    val id: Long
    @get:JvmName("getIsBot")
    val isBot: Boolean
    val firstName: String?
    val lastName: String?
    val username: String?
    val languageCode: String?
    @get:JvmName("getIsPremium")
    val isPremium: Boolean
}

/**
 * Telegram [User](https://core.telegram.org/bots/api#user) type.
 *
 * This class does not have fields which are marked as "Returned only in `getMe`"
 *
 * For up-to-date documentation please consult the official Telegram docs.
 *
 * @see Me
 */
data class User(
    override val id: Long,
    @get:JvmName("getIsBot")
    override val isBot: Boolean = false,
    override val firstName: String? = null,
    override val lastName: String? = null,
    override val username: String? = null,
    override val languageCode: String? = null,
    @get:JvmName("getIsPremium")
    override val isPremium: Boolean = false
) : IUser {
    /**
     * Telegram [User](https://core.telegram.org/bots/api#user) type, including fields,
     * marked as "Returned only in `getMe`".
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @Suppress("INAPPLICABLE_JVM_NAME")
    data class Me(
        override val id: Long,
        override val firstName: String? = null,
        override val lastName: String? = null,
        override val username: String? = null,
        override val languageCode: String? = null,
        @get:JvmName("getIsPremium")
        override val isPremium: Boolean = false,
        val addedToAttachmentMenu: Boolean = false,
        val canJoinGroups: Boolean = false,
        val canReadAllGroupMessages: Boolean = false,
        val supportsInlineQueries: Boolean = false
    ) : IUser {
        @get:JvmName("getIsBot")
        override val isBot: Boolean = true
    }
}
