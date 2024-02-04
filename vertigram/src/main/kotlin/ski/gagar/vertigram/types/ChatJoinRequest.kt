package ski.gagar.vertigram.types

import java.time.Instant

data class ChatJoinRequest(
    val chat: ChatMember,
    val from: User,
    val date: Instant,
    val bio: String? = null,
    val inviteLink: ChatInviteLink? = null,
    // Since Telegram Bot API 6.5
    val userChatId: Long
)
