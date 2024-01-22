package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class MessageOriginUser(
    val date: Instant,
    val senderUser: User
) : MessageOrigin {
    override val type: MessageOriginType = MessageOriginType.USER
}
