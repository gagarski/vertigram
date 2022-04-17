package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class ChatJoinRequest(
    val chat: ChatMember,
    val from: User,
    val date: Instant,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null
)
