package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore

data class ChatMemberAdministrator(
    override val user: User,
    val canBeEdited: Boolean = false,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    val canManageChat: Boolean = false,
    val canDeleteMessages: Boolean = false,
    val canManageVideoChats: Boolean = false,
    val canRestrictMembers: Boolean = false,
    val canPromoteMembers: Boolean = false,
    val canChangeInfo: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canPostMessages: Boolean = false,
    val canEditMessages: Boolean = false,
    val canPinMessages: Boolean = false,
    val customTitle: String? = null,
    // Since Telegram Bot Api 6.3
    val canManageTopics: Boolean = false,
    // Since Telegram Bot Api 6.9
    val canPostStories: Boolean = false,
    val canEditStories: Boolean = false,
    val canDeleteStories: Boolean = false,
) : ChatMember {
    override val status: ChatMemberStatus = ChatMemberStatus.ADMINISTRATOR
    @JsonIgnore
    override val isMember: Boolean = true
}
