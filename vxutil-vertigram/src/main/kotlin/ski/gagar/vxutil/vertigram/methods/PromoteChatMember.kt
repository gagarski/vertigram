package ski.gagar.vxutil.vertigram.methods

data class PromoteChatMember(
    val chatId: Long,
    val userId: Long,
    val canChangeInfo: Boolean = false,
    val canPostMessages: Boolean = false,
    val canEditMessages: Boolean = false,
    val canDeleteMessages: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canRestrictMembers: Boolean = false,
    val canPinMessages: Boolean = false,
    val canPromoteMembers: Boolean = false
) : JsonTgCallable<Boolean>()
