package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class LeaveChat(
    val chatId: ChatId
) : JsonTgCallable<Boolean>()
