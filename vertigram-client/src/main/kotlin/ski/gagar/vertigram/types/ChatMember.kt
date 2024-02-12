package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.util.nullIfEpoch
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [ChatMember](https://core.telegram.org/bots/api#chatmember) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "status")
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
     * Telegram [ChatMemberAdministrator](https://core.telegram.org/bots/api#chatmemberadministrator) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Administrator(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val user: User,
        val canBeEdited: Boolean = false,
        @get:JvmName("getIsAnonymous")
        val isAnonymous: Boolean = false,
        val canManageChat: Boolean = false,
        val canDeleteMessages: Boolean = false,
        val canManageVideoChats: Boolean = false,
        val canRestrictMembers: Boolean = false,
        val canPromoteMembers: Boolean = false,
        val canChangeInfo: Boolean = false,
        val canInviteUsers: Boolean = false,
        val canPostMessages: Boolean = false,
        val canEditMessages: Boolean = false,
        val canPinMessages: Boolean = false,
        val canPostStories: Boolean = false,
        val canEditStories: Boolean = false,
        val canDeleteStories: Boolean = false,
        val canManageTopics: Boolean = false,
        val customTitle: String? = null,
    ) : ChatMember {
        override val status: Status = Status.ADMINISTRATOR
        @JsonIgnore
        override val isMember: Boolean = true
    }

    /**
     * Telegram [ChatMemberBanned](https://core.telegram.org/bots/api#chatmemberbanned) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Banned(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val user: User,
        /**
         * Date when restrictions will be lifted for this user; Unix time.
         *
         * If [Instant.EPOCH], then the user is banned forever
         * Use [ski.gagar.vertigram.types.util.orEpoch] if you want to initialize it from nullable [Instant]
         */
        private val untilDate: Instant
    ) : ChatMember {
        override val status: Status = Status.BANNED
        @JsonIgnore
        override val isMember: Boolean = false
        @get:JsonIgnore
        val until: Instant?
            get() = untilDate.nullIfEpoch()
    }

    /**
     * Telegram [ChatMemberLeft](https://core.telegram.org/bots/api#chatmemberleft) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Left(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val user: User
    ) : ChatMember {
        override val status: Status = Status.LEFT
        @JsonIgnore
        override val isMember: Boolean = false
    }

    /**
     * Telegram [ChatMemberMember](https://core.telegram.org/bots/api#chatmembermember) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Member(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val user: User
    ) : ChatMember {
        override val status: Status = ChatMember.Status.MEMBER
        @JsonIgnore
        override val isMember: Boolean = true
    }


    /**
     * Telegram [ChatMemberOwner](https://core.telegram.org/bots/api#chatmemberowner) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Owner(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val user: User,
        @get:JvmName("getIsAnonymous")
        val isAnonymous: Boolean = false,
        val customTitle: String? = null
    ) : ChatMember {
        override val status: Status = Status.OWNER
        @JsonIgnore
        override val isMember: Boolean = true
    }

    /**
     * Telegram [ChatMemberRestricted](https://core.telegram.org/bots/api#chatmemberrestricted) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Restricted(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val user: User,
        @get:JvmName("getIsMember")
        override val isMember: Boolean = false,
        val canSendMessages: Boolean = false,
        val canSendAudios: Boolean = false,
        val canSendDocuments: Boolean = false,
        val canSendPhotos: Boolean = false,
        val canSendVideos: Boolean = false,
        val canSendVideoNotes: Boolean = false,
        val canSendVoiceNotes: Boolean = false,
        val canSendPolls: Boolean = false,
        val canSendOtherMessages: Boolean = false,
        val canAddWebPagePreviews: Boolean = false,
        val canChangeInfo: Boolean = false,
        val canInviteUsers: Boolean = false,
        val canPinMessages: Boolean = false,
        /**
         * Date when restrictions will be lifted for this user; Unix time.
         *
         * If [Instant.EPOCH], then the user is banned forever
         * Use [ski.gagar.vertigram.types.util.orEpoch] if you want to initialize it from nullable [Instant]
         */
        private val untilDate: Instant,
        val canManageTopics: Boolean = false
    ) : ChatMember {
        override val status: Status = Status.RESTRICTED
        @get:JsonIgnore
        val until: Instant? = untilDate.nullIfEpoch()
    }

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

