package ski.gagar.vertigram.types

import java.time.Instant

data class MessageOriginChat(
    val date: Instant,
    val senderChat: Chat,
    val authorSignature: String? = null
) : MessageOrigin {
    override val type: MessageOriginType = MessageOriginType.CHAT
}
