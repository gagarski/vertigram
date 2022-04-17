package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class ChatMemberUpdated(
    val chat: Chat,
    val from: User,
    val date: Instant,
    val oldChatmember: ChatMember,
    val newChatMember: ChatMember,
    val inviteLink: ChatInviteLink? = null
)
