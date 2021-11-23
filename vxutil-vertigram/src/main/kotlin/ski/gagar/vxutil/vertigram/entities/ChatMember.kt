package ski.gagar.vxutil.vertigram.entities

import ski.gagar.vxutil.vertigram.util.TgEnumName
import java.time.Instant


enum class ChatMemberStatus(val here: Boolean) {
    @TgEnumName("creator")
    CREATOR(true),
    @TgEnumName("administrator")
    ADMINISTRATOR(true),
    @TgEnumName("member")
    MEMBER(true),
    @TgEnumName("restricted")
    RESTRICTED(true),
    @TgEnumName("left")
    LEFT(false),
    @TgEnumName("kicked")
    KICKED(false)
}

data class ChatMember(
    val user: User,
    val status: ChatMemberStatus,
    val untilDate: Instant? = null,
    // TODO deal with these Boolean?s
    val canBeEdited: Boolean? = null,
    val canChangeInfo: Boolean? = null,
    val canPostMessages: Boolean? = null,
    val canEditMessages: Boolean? = null,
    val canDeleteMessages: Boolean? = null,
    val canInviteUsers: Boolean? = null,
    val canRestrictMembers: Boolean? = null,
    val canPinMessages: Boolean? = null,
    val canPromoteMembers: Boolean? = null,
    @get:JvmName("getIsMember")
    val isMember: Boolean? = null,
    val canSendMessages: Boolean? = null,
    val canSendMediaMessages: Boolean? = null,
    val canSendOtherMessages: Boolean? = null,
    val canAddWebPagePreviews: Boolean? = null,
    val customTitle: String? = null
)
