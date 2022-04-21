package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ChatMemberBanned(
    override val user: User,
    @JsonProperty("untilDate")
    @Deprecated("Access through untilDate instead")
    val untilDateRaw: Instant? = null
) : ChatMember {
    override val status: ChatMemberStatus = ChatMemberStatus.BANNED
    @JsonIgnore
    override val isMember: Boolean = false
    @Suppress("DEPRECATION")
    val untilDate: Instant?
        get() = untilDateRaw?.let {
            if (untilDateRaw.toEpochMilli() == 0L) null else untilDateRaw
        }
}
