package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class SetChatDescription(
    val chatId: ChatId,
    val description: String
) : JsonTgCallable<Boolean>()
