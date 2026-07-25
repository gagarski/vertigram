package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.AccentColor
import ski.gagar.vertigram.telegram.types.colors.ProfileAccentColor
import java.time.Duration
import java.time.Instant

/**
 * Common interface for [Chat] and [Chat.FullInfo]
 */
interface IChat {
    val id: Long
    val type: Chat.Type
    val title: String?
    val username: String?
    val firstName: String?
    val lastName: String?
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("getIsForum")
    val isForum: Boolean
}

/**
 * This object represents a chat.
 *
 * See Telegram's [Chat](https://core.telegram.org/bots/api#chat) documentation.
 */
@TelegramCodegen.Type
data class Chat internal constructor(
    /** Unique identifier for this chat. */
    override val id: Long,
    /** Type of the chat: private, group, supergroup or channel. */
    override val type: Type,
    /** Title, for supergroups, channels and group chats. */
    override val title: String? = null,
    /** Username, for private chats, supergroups and channels if available. */
    override val username: String? = null,
    /** First name of the other party in a private chat. */
    override val firstName: String? = null,
    /** Last name of the other party in a private chat. */
    override val lastName: String? = null,
    /** `true` if the supergroup chat is a forum with topics enabled. */
    @get:JvmName("getIsForum")
    override val isForum: Boolean = false,
    /** `true` if the chat is the direct messages chat of a channel. */
    @get:JvmName("getIsDirectMessages")
    val isDirectMessages: Boolean = false
) : IChat {
    /**
     * This object contains full information about a chat, returned by
     * [getChat][ski.gagar.vertigram.telegram.methods.getChat].
     *
     * See Telegram's [ChatFullInfo](https://core.telegram.org/bots/api#chatfullinfo) documentation.
     */
    @TelegramCodegen.Type
    data class FullInfo internal constructor(
        /** Unique identifier for this chat. */
        override val id: Long,
        /** Type of the chat: private, group, supergroup or channel. */
        override val type: Type,
        /** Title, for supergroups, channels and group chats. */
        override val title: String? = null,
        /** Username, for private chats, supergroups and channels if available. */
        override val username: String? = null,
        /** First name of the other party in a private chat. */
        override val firstName: String? = null,
        /** Last name of the other party in a private chat. */
        override val lastName: String? = null,
        /** `true` if the supergroup chat is a forum with topics enabled. */
        @get:JvmName("getIsForum")
        override val isForum: Boolean = false,
        /** `true` if the chat is the direct messages chat of a channel. */
        @get:JvmName("getIsDirectMessages")
        val isDirectMessages: Boolean = false,
        /** Chat photo. */
        val photo: Photo? = null,
        /** If non-empty, the list of all active chat usernames; for private chats, supergroups and channels. */
        val activeUsernames: List<String>? = null,
        /** For private chats, the date of birth of the user. */
        val birthdate: Birthdate? = null,
        /** For private chats with business accounts, the intro of the business. */
        val businessIntro: BusinessIntro? = null,
        /** For private chats with business accounts, the location of the business. */
        val businessLocation: BusinessLocation? = null,
        /** For private chats with business accounts, the opening hours of the business. */
        val businessOpeningHours: BusinessOpeningHours? = null,
        /** For private chats, the personal channel of the user. */
        val personalChat: Chat? = null,
        /** The bot that processes join request queries in the chat; available only to chat administrators. */
        val guardBot: User? = null,
        /** The community to which the chat belongs. */
        val community: Community? = null,
        /** List of available reactions allowed in the chat. */
        val availableReactions: List<Reaction>? = null,
        /**
         * Identifier of the accent color for the chat name and backgrounds of the chat photo, reply header, and link
         * preview.
         */
        val accentColorId: Int,
        /** The maximum number of reactions that can be set on a message in the chat. */
        val maxReactionCount: Int,
        /** Custom emoji identifier of the emoji chosen by the chat for the reply header and link preview background. */
        val backgroundCustomEmojiId: Int? = null,
        /** Identifier of the accent color for the chat's profile background. */
        val profileAccentColorId: Int? = null,
        /** Custom emoji identifier of the emoji chosen by the chat for its profile background. */
        val profileBackgroundCustomEmojiId: Int? = null,
        /** Custom emoji identifier of the emoji status of the chat or the other party in a private chat. */
        val emojiStatusCustomEmojiId: String? = null,
        /** Expiration date of the emoji status of the chat or the other party in a private chat, if any. */
        val emojiStatusExpirationDate: Instant? = null,
        /** Bio of the other party in a private chat. */
        val bio: String? = null,
        /**
         * `true` if privacy settings of the other party in the private chat allow `tg://user?id=<user_id>` links only
         * in chats with the user.
         */
        val hasPrivateForwards: Boolean = false,
        /**
         * `true` if the privacy settings of the other party restrict sending voice and video note messages in the
         * private chat.
         */
        val hasRestrictedVoiceAndVideoMessages: Boolean = false,
        /** `true` if users need to join the supergroup before they can send messages. */
        val joinToSendMessages: Boolean = false,
        /**
         * `true` if all users directly joining the supergroup without using an invite link need to be approved by
         * supergroup administrators.
         */
        val joinByRequest: Boolean = false,
        /** Description, for groups, supergroups and channel chats. */
        val description: String? = null,
        /** Primary invite link, for groups, supergroups and channel chats. */
        val inviteLink: String? = null,
        /** The most recent pinned message by sending date. */
        val pinnedMessage: Message? = null,
        /** Default chat member permissions, for groups and supergroups. */
        val permissions: ChatPermissions? = null,
        /** Information about types of gifts accepted by the chat or corresponding user for private chats. */
        val acceptedGiftTypes: AcceptedGiftTypes,
        /** `true` if paid media messages can be sent or forwarded to the channel chat; for channel chats only. */
        val canSendPaidMedia: Boolean = false,
        /** For private chats, the rating of the user if any. */
        val rating: UserRating? = null,
        /** For private chats, the first audio added to the profile of the user. */
        val firstProfileAudio: Audio? = null,
        /** The color scheme based on a unique gift used for the chat's name, message replies and link previews. */
        val uniqueGiftColors: UniqueGift.Colors? = null,
        /** The number of Telegram Stars a general user has to pay to send a message to the chat. */
        val paidMessageStarCount: Int? = null,
        /** For supergroups, the minimum allowed delay between messages sent by each unprivileged user. */
        val slowModeDelay: Duration? = null,
        /**
         * For supergroups, the minimum number of boosts a non-administrator user needs to add to ignore slow mode and
         * chat permissions.
         */
        val unrestrictBoostCount: Int? = null,
        /** The time after which all messages sent to the chat will be automatically deleted. */
        val messageAutoDeleteTime: Duration? = null,
        /** `true` if aggressive anti-spam checks are enabled in the supergroup; available only to administrators. */
        val hasAggressiveAntiSpamEnabled: Boolean? = null,
        /** `true` if non-administrators can only get the list of bots and administrators in the chat. */
        val hasHiddenMembers: Boolean = false,
        /** `true` if messages from the chat can't be forwarded to other chats. */
        val hasProtectedContent: Boolean = false,
        /** `true` if new chat members have access to old messages; available only to chat administrators. */
        val hasVisibleHistory: Boolean = false,
        /** For supergroups, name of the group sticker set. */
        val stickerSetName: String? = null,
        /** `true` if the bot can change the group sticker set. */
        val canSetStickerSet: Boolean = false,
        /**
         * For supergroups, the name of the group's custom emoji sticker set. Custom emoji from this set can be used by
         * all users and bots in the group.
         */
        val customEmojiStickerSetName: String? = null,
        /**
         * Unique identifier for the linked chat, i.e. the discussion group identifier for a channel and vice versa;
         * for supergroups and channel chats.
         */
        val linkedChatId: Long? = null,
        /** Information about the corresponding channel chat; for direct messages chats only. */
        val parentChat: Chat? = null,
        /** For supergroups, the location to which the supergroup is connected. */
        val location: Location? = null,
    ) : IChat{
        /**
         * [AccentColor] color enum value with given [accentColorId], or null if the id is unknown
         */
        @get:JsonIgnore
        val accentColor: AccentColor?
            get() = AccentColor.byId[accentColorId]
        /**
         * [ProfileAccentColor] color enum value with given [profileAccentColorId], or null if the id is unknown
         */
        @get:JsonIgnore
        val profileAccentColor: ProfileAccentColor?
            get() = ProfileAccentColor.byId[accentColorId]

        companion object
    }


    /**
     * This object represents a chat photo.
     *
     * See Telegram's [ChatPhoto](https://core.telegram.org/bots/api#chatphoto) documentation.
     */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        /** File identifier of the small (160x160) chat photo, usable for download while the photo is unchanged. */
        val smallFileId: String,
        /**
         * Unique file identifier of the small (160x160) chat photo, which is supposed to be the same over time and for
         * different bots. Can't be used to download or reuse the file.
         */
        val smallFileUniqueId: String,
        /** File identifier of the big (640x640) chat photo, usable for download while the photo is unchanged. */
        val bigFileId: String,
        /**
         * Unique file identifier of the big (640x640) chat photo, which is supposed to be the same over time and for
         * different bots. Can't be used to download or reuse the file.
         */
        val bigFileUniqueId: String
    ) {
        companion object
    }

    /**
     * Represents a location to which a chat is connected.
     *
     * See Telegram's [ChatLocation](https://core.telegram.org/bots/api#chatlocation) documentation.
     */
    @TelegramCodegen.Type
    data class Location internal constructor(
        /** The location to which the supergroup is connected. Can't be a live location. */
        val location: ski.gagar.vertigram.telegram.types.Location,
        /** Location address; 1-64 characters, as defined by the chat owner. */
        val address: String
    ) {
        companion object
    }


    enum class Type(val group: Boolean) {
        @JsonProperty("private")
        PRIVATE(false),
        @JsonProperty("group")
        GROUP(true),
        @JsonProperty("supergroup")
        SUPERGROUP(true),
        @JsonProperty("channel")
        CHANNEL(false)
    }
    companion object
}

