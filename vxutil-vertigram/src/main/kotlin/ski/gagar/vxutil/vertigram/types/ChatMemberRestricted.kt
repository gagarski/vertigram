package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class ChatMemberRestricted(
    override val user: User,
    @get:JvmName("getIsMember")
    override val isMember: Boolean = false,
    val canChangeInfo: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canPinMessages: Boolean = false,
    val canSendMessages: Boolean = false,
    val canSendPolls: Boolean = false,
    val canSendOtherMessages: Boolean = false,
    val canAddWebPagePreviews: Boolean = false,
    @JsonProperty("untilDate")
    @Deprecated("Access through untilDate instead")
    val untilDateRaw: Instant,
    // Since Telegram Bot API 6.3
    val canManageTopics: Boolean = false,
    // Since Telegram Bot API 6.5
    val canSendAudios: Boolean = false,
    val canSendDocuments: Boolean = false,
    val canSendPhotos: Boolean = false,
    val canSendVideos: Boolean = false,
    val canSendVideoNotes: Boolean = false,
    val canSendVoiceNotes: Boolean = false,

) : ChatMember {
    override val status: ChatMemberStatus = ChatMemberStatus.RESTRICTED
    @Suppress("DEPRECATION")
    val untilDate: Instant?
        get() = if (untilDateRaw.toEpochMilli() == 0L) null else untilDateRaw
}
