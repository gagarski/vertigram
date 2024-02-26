package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [ChatInviteLink](https://core.telegram.org/bots/api#chatinvitelink) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ChatInviteLink(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val inviteLink: String,
    val creator: User,
    val createsJoinRequest: Boolean = false,
    @get:JvmName("getIsPrimary")
    val isPrimary: Boolean = false,
    @get:JvmName("getIsRevoked")
    val isRevoked: Boolean = false,
    val name: String? = null,
    val expireDate: Instant? = null,
    val memberLimit: Int? = null,
    val pendingJoinRequestCount: Int? = null
)
