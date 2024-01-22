package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class MessageOriginHiddenUser(
    val date: Instant,
    val senderUserName: String
) : MessageOrigin {
    override val type: MessageOriginType = MessageOriginType.HIDDEN_USER
}
