package ski.gagar.vxutil.vertigram.types

import java.time.Duration

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
    // Since Telegram Bot Api 6.3
    val emojiStatusCustomEmojiId: String? = null
)
