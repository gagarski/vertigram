package ski.gagar.vxutil.vertigram.entities

import ski.gagar.vxutil.vertigram.util.TgEnumName

enum class ChatType(val group: Boolean) {
    @TgEnumName("private")
    PRIVATE(false),
    @TgEnumName("group")
    GROUP(true),
    @TgEnumName("supergroup")
    SUPERGROUP(true),
    @TgEnumName("channel")
    CHANNEL(false)
}

data class Chat(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val photo: ChatPhoto? = null
)

data class ChatVerbose(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val photo: ChatPhoto? = null,
    val description: String? = null,
    val inviteLink: String? = null,
    val pinnedMessage: Message? = null,
    val permissions: ChatPermissions? = null,
    val slowModeDelay: Long? = null,
    val stickerSetName: String? = null,
    val canSetStickerSet: Boolean = false  // Optional by specification, empty value is treated as false
)
