package ski.gagar.vxutil.vertigram.types

data class ChatAdministratorRights(
    @get:JvmName("getIsAnonymous")
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
)
