package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * Telegram type ChatMemberRestricted.
 */
data class ChatMemberRestricted(
    override val user: User,
    override val isMember: Boolean,
    val canChangeInfo: Boolean,
    val canInviteUsers: Boolean,
    val canPinMessages: Boolean,
    val canSendMessages: Boolean,
    val canSendMediaMessages: Boolean,
    val canSendPolls: Boolean,
    val canSendOtherMessages: Boolean,
    val canAddWebPagePreviews: Boolean,
    @JsonProperty("untilDate")
    @Deprecated("Access through untilDate instead")
    val untilDateRaw: Instant

) : ChatMember() {
    override val status: ChatMemberStatus = ChatMemberStatus.RESTRICTED
    @Suppress("DEPRECATION")
    val untilDate: Instant?
        get() = if (untilDateRaw.toEpochMilli() == 0L) null else untilDateRaw
}
