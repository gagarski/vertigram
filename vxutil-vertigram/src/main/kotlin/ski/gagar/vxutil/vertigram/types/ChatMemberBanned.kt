package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * Telegram type ChatMemberBanned.
 */
data class ChatMemberBanned(
    override val user: User,
    @JsonProperty("untilDate")
    @Deprecated("Access through untilDate instead")
    val untilDateRaw: Instant
) : ChatMember() {
    override val status: ChatMemberStatus = ChatMemberStatus.BANNED
    override val isMember: Boolean = false
    @Suppress("DEPRECATION")
    val untilDate: Instant?
        get() = if (untilDateRaw.toEpochMilli() == 0L) null else untilDateRaw
}
