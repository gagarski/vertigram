package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class UnpinChatMessage(
    val chatId: ChatId,
    val messageId: Long
) : JsonTgCallable<Boolean>()
