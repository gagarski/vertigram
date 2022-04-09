package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class PromoteChatMember(
    val chatId: ChatId,
    val userId: Long,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    val canManageChat: Boolean = false,
    val canPostMessages: Boolean = false,
    val canEditMessages: Boolean = false,
    val canDeleteMessages: Boolean = false,
    val canManageVoiceChats: Boolean = false,
    val canRestrictMembers: Boolean = false,
    val canPromoteMembers: Boolean = false,
    val canChangeInfo: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canPinMessages: Boolean = false
) : JsonTgCallable<Boolean>()
