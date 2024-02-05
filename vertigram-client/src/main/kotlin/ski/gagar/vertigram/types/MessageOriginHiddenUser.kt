package ski.gagar.vertigram.types

import java.time.Instant

data class MessageOriginHiddenUser(
    val date: Instant,
    val senderUserName: String
) : MessageOrigin {
    override val type: MessageOriginType = MessageOriginType.HIDDEN_USER
}
