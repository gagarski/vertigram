package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type ChatMemberAdministrator.
 */
data class ChatMemberAdministrator(
    override val user: User,
    val canBeEdited: Boolean,
    val isAnonymous: Boolean,
    val canManageChat: Boolean,
    val canDeleteMessages: Boolean,
    val canManageVoiceChats: Boolean,
    val canRestrictMembers: Boolean,
    val canPromoteMembers: Boolean,
    val canChangeInfo: Boolean,
    val canInviteUsers: Boolean,
    val canPostMessages: Boolean,
    val canEditMessages: Boolean,
    val canPinMessages: Boolean,
    val customTitle: String
) : ChatMember() {
    override val status: ChatMemberStatus = ChatMemberStatus.ADMINISTRATOR
    override val isMember: Boolean = true
}
