package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration
import java.time.Instant

/**
 * Represents an invite link for a chat.
 *
 * See Telegram's [ChatInviteLink](https://core.telegram.org/bots/api#chatinvitelink) documentation.
 */
@TelegramCodegen.Type
data class ChatInviteLink internal constructor(
    /**
     * The invite link. If the link was created by another chat administrator, then the second part of the link will be
     * replaced with “…”.
     */
    val inviteLink: String,
    /** Creator of the link. */
    val creator: User,
    /** `true` if users joining the chat via the link need to be approved by chat administrators. */
    val createsJoinRequest: Boolean = false,
    /** `true` if the link is primary. */
    @get:JvmName("getIsPrimary")
    val isPrimary: Boolean = false,
    /** `true` if the link is revoked. */
    @get:JvmName("getIsRevoked")
    val isRevoked: Boolean = false,
    /** Invite link name. */
    val name: String? = null,
    /** Point in time when the link will expire or has expired. */
    val expireDate: Instant? = null,
    /**
     * The maximum number of users that can be members of the chat simultaneously after joining via this invite link;
     * 1-99999.
     */
    val memberLimit: Int? = null,
    /** Number of pending join requests created using this link. */
    val pendingJoinRequestCount: Int? = null,
    /** The duration the subscription will be active before the next payment. */
    val subscriptionPeriod: Duration? = null,
    /**
     * The amount of Telegram Stars a user must pay initially and after each subsequent subscription period to be a
     * member of the chat using the link.
     */
    val subscriptionPrice: Int? = null
) {
    companion object
}
