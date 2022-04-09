package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class SetChatAdministratorCustomTitle(
    val chatId: ChatId,
    val userId: Long,
    val customTitle: String
) : JsonTgCallable<Boolean>()
