package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.util.nullIfEpoch
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant


/**
 * This object contains information about one member of a chat.
 *
 * See Telegram's [ChatMember](https://core.telegram.org/bots/api#chatmember) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = ChatMember.Owner::class, name = ChatMember.Status.OWNER_STR),
    JsonSubTypes.Type(value = ChatMember.Administrator::class, name = ChatMember.Status.ADMINISTRATOR_STR),
    JsonSubTypes.Type(value = ChatMember.Member::class, name = ChatMember.Status.MEMBER_STR),
    JsonSubTypes.Type(value = ChatMember.Restricted::class, name = ChatMember.Status.RESTRICTED_STR),
    JsonSubTypes.Type(value = ChatMember.Left::class, name = ChatMember.Status.LEFT_STR),
    JsonSubTypes.Type(value = ChatMember.Banned::class, name = ChatMember.Status.BANNED_STR),
)
sealed interface ChatMember {
    val status: Status
    @get:JsonIgnore
    val isMember: Boolean
    val user: User

    /**
     * Represents a [chat member][ChatMember] that has some additional privileges.
     *
     * See Telegram's [ChatMemberAdministrator](https://core.telegram.org/bots/api#chatmemberadministrator)
     * documentation.
     */
    @TelegramCodegen.Type
    data class Administrator internal constructor(
        /** Information about the user. */
        override val user: User,
        /** `true` if the bot is allowed to edit administrator privileges of that user. */
        val canBeEdited: Boolean = false,
        /** `true` if the user's presence in the chat is hidden. */
        @get:JvmName("getIsAnonymous")
        val isAnonymous: Boolean = false,
        /**
         * `true` if the administrator can access the chat event log, get the boost list, see hidden supergroup and
         * channel members, report spam messages, ignore slow mode, and send messages to the chat without paying
         * Telegram Stars. Implied by any other administrator privilege.
         */
        val canManageChat: Boolean = false,
        /** `true` if the administrator can delete messages of other users. */
        val canDeleteMessages: Boolean = false,
        /** `true` if the administrator can manage video chats. */
        val canManageVideoChats: Boolean = false,
        /** `true` if the administrator can restrict, ban or unban chat members, or access supergroup statistics. */
        val canRestrictMembers: Boolean = false,
        /**
         * `true` if the administrator can add new administrators with a subset of their own privileges or demote
         * administrators that they have promoted, directly or indirectly.
         */
        val canPromoteMembers: Boolean = false,
        /** `true` if the user is allowed to change the chat title, photo and other settings. */
        val canChangeInfo: Boolean = false,
        /** `true` if the user is allowed to invite new users to the chat. */
        val canInviteUsers: Boolean = false,
        /**
         * `true` if the administrator can post messages in the channel, approve suggested posts, or access channel
         * statistics; for channels only.
         */
        val canPostMessages: Boolean = false,
        /** `true` if the administrator can edit messages of other users and can pin messages; for channels only. */
        val canEditMessages: Boolean = false,
        /** `true` if the user is allowed to pin messages; for groups and supergroups only. */
        val canPinMessages: Boolean = false,
        /** `true` if the administrator can post stories to the chat. */
        val canPostStories: Boolean = false,
        /**
         * `true` if the administrator can edit stories posted by other users, post stories to the chat page, pin chat
         * stories, and access the chat's story archive.
         */
        val canEditStories: Boolean = false,
        /** `true` if the administrator can delete stories posted by other users. */
        val canDeleteStories: Boolean = false,
        /** `true` if the user is allowed to create, rename, close, and reopen forum topics; for supergroups only. */
        val canManageTopics: Boolean = false,
        /**
         * `true` if the administrator can manage direct messages of the channel and decline suggested posts; for
         * channels only.
         */
        val canManageDirectMessages: Boolean = false,
        /** `true` if the administrator can edit the tags of regular members; for groups and supergroups only. */
        val canManageTags: Boolean = false,
        /** Custom title for this user. */
        val customTitle: String? = null,
    ) : ChatMember {
        override val status: Status = Status.ADMINISTRATOR
        @JsonIgnore
        override val isMember: Boolean = true

        companion object
    }

    /**
     * Represents a [chat member][ChatMember] that was banned in the chat and can't return to the chat or view chat
     * messages.
     *
     * See Telegram's [ChatMemberBanned](https://core.telegram.org/bots/api#chatmemberbanned) documentation.
     */
    @TelegramCodegen.Type
    data class Banned internal constructor(
        /** Information about the user. */
        override val user: User,
        /**
         * Date when restrictions will be lifted for this user.
         *
         * [Instant.EPOCH] means the user is banned forever. Use
         * [ski.gagar.vertigram.telegram.types.util.orEpoch] to initialize it from a nullable [Instant].
         */
        @PublishedApi internal val untilDate: Instant
    ) : ChatMember {
        override val status: Status = Status.BANNED
        @JsonIgnore
        override val isMember: Boolean = false
        @get:JsonIgnore
        val until: Instant?
            get() = untilDate.nullIfEpoch()

        companion object
    }

    /**
     * Represents a [chat member][ChatMember] that isn't currently a member of the chat, but may join it themselves.
     *
     * See Telegram's [ChatMemberLeft](https://core.telegram.org/bots/api#chatmemberleft) documentation.
     */
    @TelegramCodegen.Type
    data class Left internal constructor(
        /** Information about the user. */
        override val user: User
    ) : ChatMember {
        override val status: Status = Status.LEFT
        @JsonIgnore
        override val isMember: Boolean = false

        companion object
    }

    /**
     * Represents a [chat member][ChatMember] that has no additional privileges or restrictions.
     *
     * See Telegram's [ChatMemberMember](https://core.telegram.org/bots/api#chatmembermember) documentation.
     */
    @TelegramCodegen.Type
    data class Member internal constructor(
        /** Information about the user. */
        override val user: User,
        /** Tag of the member. */
        val tag: String? = null,
        /** Date when the user's subscription will expire. */
        val untilDate: Instant? = null
    ) : ChatMember {
        override val status: Status = ChatMember.Status.MEMBER
        @JsonIgnore
        override val isMember: Boolean = true
        companion object
    }


    /**
     * Represents a [chat member][ChatMember] that owns the chat and has all administrator privileges.
     *
     * See Telegram's [ChatMemberOwner](https://core.telegram.org/bots/api#chatmemberowner) documentation.
     */
    @TelegramCodegen.Type
    data class Owner internal constructor(
        /** Information about the user. */
        override val user: User,
        /** `true` if the user's presence in the chat is hidden. */
        @get:JvmName("getIsAnonymous")
        val isAnonymous: Boolean = false,
        /** Custom title for this user. */
        val customTitle: String? = null
    ) : ChatMember {
        override val status: Status = Status.OWNER
        @JsonIgnore
        override val isMember: Boolean = true

        companion object
    }

    /**
     * Represents a [chat member][ChatMember] that is under certain restrictions in the chat. Supergroups only.
     *
     * See Telegram's [ChatMemberRestricted](https://core.telegram.org/bots/api#chatmemberrestricted) documentation.
     */
    @TelegramCodegen.Type
    data class Restricted internal constructor(
        /** Information about the user. */
        override val user: User,
        /** `true` if the user is a member of the chat at the moment of the request. */
        @get:JvmName("getIsMember")
        override val isMember: Boolean = false,
        /**
         * `true` if the user is allowed to send text messages, rich messages, contacts, giveaways, giveaway winners,
         * invoices, locations and venues.
         */
        val canSendMessages: Boolean = false,
        /** `true` if the user is allowed to send audios. */
        val canSendAudios: Boolean = false,
        /** `true` if the user is allowed to send documents. */
        val canSendDocuments: Boolean = false,
        /** `true` if the user is allowed to send photos. */
        val canSendPhotos: Boolean = false,
        /** `true` if the user is allowed to send videos. */
        val canSendVideos: Boolean = false,
        /** `true` if the user is allowed to send video notes. */
        val canSendVideoNotes: Boolean = false,
        /** `true` if the user is allowed to send voice notes. */
        val canSendVoiceNotes: Boolean = false,
        /** `true` if the user is allowed to send polls and checklists. */
        val canSendPolls: Boolean = false,
        /** `true` if the user is allowed to send animations, games, stickers and use inline bots. */
        val canSendOtherMessages: Boolean = false,
        /** `true` if the user is allowed to add web page previews to their messages. */
        val canAddWebPagePreviews: Boolean = false,
        /** `true` if the user is allowed to react to messages. */
        val canReactToMessages: Boolean = false,
        /** `true` if the user is allowed to edit their own tag. */
        val canEditTag: Boolean = false,
        /** `true` if the user is allowed to change the chat title, photo and other settings. */
        val canChangeInfo: Boolean = false,
        /** `true` if the user is allowed to invite new users to the chat. */
        val canInviteUsers: Boolean = false,
        /** `true` if the user is allowed to pin messages. */
        val canPinMessages: Boolean = false,
        /**
         * Date when restrictions will be lifted for this user.
         *
         * [Instant.EPOCH] means the user is restricted forever. Use
         * [ski.gagar.vertigram.telegram.types.util.orEpoch] to initialize it from a nullable [Instant].
         */
        @PublishedApi internal val untilDate: Instant,
        /** `true` if the user is allowed to create forum topics. */
        val canManageTopics: Boolean = false,
        /** Tag of the member. */
        val tag: String? = null
    ) : ChatMember {
        override val status: Status = Status.RESTRICTED
        @get:JsonIgnore
        val until: Instant? = untilDate.nullIfEpoch()

        companion object
    }

    /**
     * A value for [ChatMember.status] field
     */
    enum class Status {
        @JsonProperty(OWNER_STR)
        OWNER,
        @JsonProperty(ADMINISTRATOR_STR)
        ADMINISTRATOR,
        @JsonProperty(MEMBER_STR)
        MEMBER,
        @JsonProperty(RESTRICTED_STR)
        RESTRICTED,
        @JsonProperty(LEFT_STR)
        LEFT,
        @JsonProperty(BANNED_STR)
        BANNED;

        companion object {
            const val OWNER_STR = "creator"
            const val ADMINISTRATOR_STR = "administrator"
            const val MEMBER_STR = "member"
            const val RESTRICTED_STR = "restricted"
            const val LEFT_STR = "left"
            const val BANNED_STR = "kicked"
        }
    }

}

