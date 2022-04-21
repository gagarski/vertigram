package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class ChatInviteLink(
    val inviteLink: String,
    val creator: User,
    val createsJoinRequest: Boolean = false,
    val isPrimary: Boolean = false,
    val isRevoked: Boolean = false,
    val name: String? = null,
    val expireDate: Instant? = null,
    val memberLimit: Int? = null,
    val pendingJoinRequestCount: Int? = null
)
