package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for [InlineQuery.chatType] and [Chat.type]
 */
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

/**
 * Telegram type Chat.
 *
 * Contains fields except those returned only in getChat
 */
data class Chat(
    val id: Long,
    val type: ChatType,
    val title: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null
)

