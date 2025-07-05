package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration
import java.time.Instant

/**
 * Telegram [ChatInviteLink](https://core.telegram.org/bots/api#chatinvitelink) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ChatInviteLink internal constructor(
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
    val pendingJoinRequestCount: Int? = null,
    val subscriptionPeriod: Duration? = null,
    val subscriptionPrice: Int? = null
) {
    companion object
}