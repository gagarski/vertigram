package ski.gagar.vxutil.vertigram.types

import java.time.Instant

/**
 * Telegram type ChatInviteLink.
 */
data class ChatInviteLink(
    val inviteLink: String,
    val creator: User,
    val createsJoinRequest: Boolean,
    val isPrimary: Boolean,
    val isRevoked: Boolean,
    val name: String? = null,
    val expireDate: Instant? = null,
    val memberLimit: Long? = null,
    val pendingJoinRequestCount: Long? = null
)
