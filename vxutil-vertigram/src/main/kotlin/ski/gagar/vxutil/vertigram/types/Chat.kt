package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Available values for [InlineQuery.chatType] and [Chat.type]
 */
enum class ChatType(val group: Boolean) {
    @JsonProperty("private")
    PRIVATE(false),
    @JsonProperty("group")
    GROUP(true),
    @JsonProperty("supergroup")
    SUPERGROUP(true),
    @JsonProperty("channel")
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

