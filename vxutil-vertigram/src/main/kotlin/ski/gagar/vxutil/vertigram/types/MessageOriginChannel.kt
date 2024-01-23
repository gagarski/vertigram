package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class MessageOriginChannel(
    val date: Instant,
    val chat: Chat,
    val messageId: Long,
    val authorSignature: String? = null
) : MessageOrigin {
    override val type: MessageOriginType = MessageOriginType.CHANNEL
}
