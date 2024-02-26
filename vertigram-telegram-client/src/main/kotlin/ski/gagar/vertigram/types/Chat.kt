package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.types.colors.AccentColor
import ski.gagar.vertigram.types.colors.ProfileAccentColor
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration
import java.time.Instant

/**
 * Common interface for [Chat] and [Chat.Verbose]
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
 * Telegram [Chat](https://core.telegram.org/bots/api#chat) type.
 *
 * This class represents a part of original Telegram type, which is not marked with
 * "Returned only in getChat". The remaining part (Always returned field + returned only in
 * [ski.gagar.vertigram.methods.GetChat] is represented by class [Chat.Verbose])
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Chat(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val id: Long,
    override val type: Type,
    override val title: String? = null,
    override val username: String? = null,
    override val firstName: String? = null,
    override val lastName: String? = null,
    @get:JvmName("getIsForum")
    override val isForum: Boolean = false
) : IChat {
    /**
     * The verbose Telegram [Chat](https://core.telegram.org/bots/api#chat) type, representing the data
     * returned from [ski.gagar.vertigram.methods.GetChat]
     */
    data class Verbose(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val id: Long,
        override val type: Type,
        override val title: String? = null,
        override val username: String? = null,
        override val firstName: String? = null,
        override val lastName: String? = null,
        @get:JvmName("getIsForum")
        override val isForum: Boolean = false,
        val photo: Photo? = null,
        val activeUserNames: List<String>? = null,
        val availableReactions: List<Reaction>? = null,
        val accentColorId: Int,
        val backgroundCustomEmojiId: Int? = null,
        val profileAccentColorId: Int? = null,
        val profileBackgroundCustomEmojiId: Int? = null,
        val emojiStatusCustomEmojiId: String? = null,
        val emojiStatusExpirationDate: Instant? = null,
        val bio: String? = null,
        val hasPrivateForwards: Boolean = false,
        val hasRestrictedVoiceAndVideoMessages: Boolean = false,
        val joinToSendMessages: Boolean = false,
        val joinByRequest: Boolean = false,
        val description: String? = null,
        val inviteLink: String? = null,
        val pinnedMessage: Message? = null,
        val permissions: ChatPermissions? = null,
        val slowModeDelay: Duration? = null,
        val unrestrictBoostCount: Int? = null,
        val messageAutoDeleteTime: Duration? = null,
        val hasAggressiveAntiSpamEnabled: Boolean? = null,
        val hasHiddenMembers: Boolean = false,
        val hasProtectedContent: Boolean = false,
        val hasVisibleHistory: Boolean = false,
        val stickerSetName: String? = null,
        val canSetStickerSet: Boolean = false,
        val customEmojiStickerSetName: String? = null,
        val linkedChatId: Long? = null,
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
    }


    /**
     * Telegram [ChatPhoto](https://core.telegram.org/bots/api#chatphoto) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Photo(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val smallFileId: String,
        val smallFileUniqueId: String,
        val bigFileId: String,
        val bigFileUniqueId: String
    )

    /**
     * Telegram [ChatLocation](https://core.telegram.org/bots/api#chatlocation) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Location(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val location: ski.gagar.vertigram.types.Location,
        val address: String
    )


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

}

