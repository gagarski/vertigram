package ski.gagar.vxutil.vertigram.types

import java.time.Duration
import java.time.Instant

data class ChatVerbose(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val photo: ChatPhoto? = null,
    val bio: String? = null,
    val hasPrivateForwards: Boolean = false,
    val hasRestrictedVoiceAndVideoMessages: Boolean = false,
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    val slowModeDelay: Duration? = null,
    val messageAutoDeleteTime: Duration? = null,
    val hasProtectedContent: Boolean = false,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean = false,
    val linkedChatId: Long? = null,
    val location: ChatLocation? = null,
    val joinToSendMessages: Boolean = false,
    val jointByRequest: Boolean = false,
    @get:JvmName("getIsForum")
    val isForum: Boolean = false,
    val activeUserNames: List<String> = listOf(),
    // Since Telegram Bot API 6.3
    val emojiStatusCustomEmojiId: String? = null,
    // Since Telegram Bot API 6.4
    val hasHiddenMembers: Boolean = false,
    val hasAggressiveAntiSpamEnabled: Boolean? = null,
    // Since Telegram Bot API 6.8
    val emojiStatusExpirationDate: Instant? = null,
    // Since Telegram Bot API 7.0
    val availableReactions: List<ReactionType>? = listOf(),
    val accentColorId: Int? = null,
    val backgroundCustomEmojiId: Int? = null,
    val profileAccentColorId: Int? = null,
    val profileBackgroundCustomEmojiId: Int? = null,
    val hasVisibleHistory: Boolean = false,
)

val ChatVerbose.accentColor: AccentColor?
    get() = AccentColor.byId[accentColorId]
val ChatVerbose.profileAccentColor: ProfileAccentColor?
    get() = ProfileAccentColor.byId[accentColorId]
