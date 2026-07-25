package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

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
 * Represents a Telegram user or bot.
 *
 * Fields returned only by [getMe][ski.gagar.vertigram.telegram.methods.getMe] are available on [Me].
 *
 * See Telegram's [User](https://core.telegram.org/bots/api#user) documentation.
 */
@TelegramCodegen.Type
data class User internal constructor(
    /** Unique identifier for this user or bot. */
    override val id: Long,
    /** Whether this user is a bot. */
    @get:JvmName("getIsBot")
    override val isBot: Boolean = false,
    /** User's or bot's first name. */
    override val firstName: String? = null,
    /** User's or bot's last name. */
    override val lastName: String? = null,
    /** User's or bot's username. */
    override val username: String? = null,
    /** IETF language tag of the user's language. */
    override val languageCode: String? = null,
    /** Whether the user is a Telegram Premium user. */
    @get:JvmName("getIsPremium")
    override val isPremium: Boolean = false
) : IUser {
    /**
     * Represents the current bot, including fields returned only by
     * [getMe][ski.gagar.vertigram.telegram.methods.getMe].
     *
     * See Telegram's [User](https://core.telegram.org/bots/api#user) documentation.
     */
    @Suppress("INAPPLICABLE_JVM_NAME")
    @TelegramCodegen.Type
    data class Me internal constructor(
        /** Unique identifier for this bot. */
        override val id: Long,
        /** Bot's first name. */
        override val firstName: String? = null,
        /** Bot's last name. */
        override val lastName: String? = null,
        /** Bot's username. */
        override val username: String? = null,
        /** IETF language tag of the bot's language. */
        override val languageCode: String? = null,
        /** Whether the bot is a Telegram Premium user. */
        @get:JvmName("getIsPremium")
        override val isPremium: Boolean = false,
        /** Whether the bot can be added to the attachment menu. */
        val addedToAttachmentMenu: Boolean = false,
        /** Whether the bot can be invited to groups. */
        val canJoinGroups: Boolean = false,
        /** Whether privacy mode is disabled for the bot. */
        val canReadAllGroupMessages: Boolean = false,
        /** Whether the bot supports inline queries. */
        val supportsInlineQueries: Boolean = false,
        /** Whether the bot can be connected to a Telegram Business account. */
        val canConnectToBusiness: Boolean = false,
        /** Whether other bots can be created to be controlled by this bot. */
        val canManageBots: Boolean = false,
        /** Whether the bot supports guest queries. */
        val supportsGuestQueries: Boolean = false,
        /** Whether the bot supports join request queries. */
        val supportsJoinRequestQueries: Boolean = false,
        /** Whether the bot has topics enabled in private chats. */
        val hasTopicsEnabled: Boolean = false,
        /** Whether users can create topics in private chats with the bot. */
        val allowsUsersToCreateTopics: Boolean = false,
        /** Whether the bot has a main Web App. */
        val hasMainWebApp: Boolean = false
    ) : IUser {
        @get:JvmName("getIsBot")
        override val isBot: Boolean = true

        companion object
    }

    companion object
}
