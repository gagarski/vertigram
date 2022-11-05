package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class PromoteChatMember(
    val chatId: ChatId,
    val userId: Long,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    val canManageChat: Boolean = false,
    val canPostMessages: Boolean = false,
    val canEditMessages: Boolean = false,
    val canDeleteMessages: Boolean = false,
    val canManageVideoChats: Boolean = false,
    val canRestrictMembers: Boolean = false,
    val canPromoteMembers: Boolean = false,
    val canChangeInfo: Boolean = false,
    val canInviteUsers: Boolean = false,
    val canPinMessages: Boolean = false,
    // Since Telegram Bot Api 6.3
    val canManageTopics: Boolean = false
) : JsonTgCallable<Boolean>()
