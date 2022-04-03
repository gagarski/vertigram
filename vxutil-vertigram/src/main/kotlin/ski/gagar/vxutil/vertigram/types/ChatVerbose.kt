package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Chat.
 *
 * Contains all fields from specification and returned from getChat Method.
 */
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
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    val slowModeDelay: Long? = null,
    val messageAutoDeleteTime: Long? = null,
    val hasProtectedContent: Boolean = false,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean = false,
    val linkedChatId: Long? = null,
    val location: ChatLocation? = null
)
