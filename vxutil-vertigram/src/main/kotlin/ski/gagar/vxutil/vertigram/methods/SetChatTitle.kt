package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class SetChatTitle(
    val chatId: ChatId,
    val title: String
) : JsonTgCallable<Boolean>()
