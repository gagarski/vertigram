package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class UnpinAllChatMessages(
    val chatId: ChatId
) : JsonTgCallable<Boolean>()
