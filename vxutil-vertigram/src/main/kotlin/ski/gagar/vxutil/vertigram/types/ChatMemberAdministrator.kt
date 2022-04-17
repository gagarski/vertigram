package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore

data class ChatMemberAdministrator(
    override val user: User,
    val canBeEdited: Boolean,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean,
    val canManageChat: Boolean,
    val canDeleteMessages: Boolean,
    val canManageVideoChats: Boolean,
    val canRestrictMembers: Boolean,
    val canPromoteMembers: Boolean,
    val canChangeInfo: Boolean,
    val canInviteUsers: Boolean,
    val canPostMessages: Boolean,
    val canEditMessages: Boolean,
    val canPinMessages: Boolean,
    val customTitle: String
) : ChatMember {
    override val status: ChatMemberStatus = ChatMemberStatus.ADMINISTRATOR
    @JsonIgnore
    override val isMember: Boolean = true
}
