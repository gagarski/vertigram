package ski.gagar.vxutil.vertigram.types

data class MessageEntity(
    val type: MessageEntityType,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null,
    val customEmojiId: String? = null
)

