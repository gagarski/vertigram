package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class SetChatDescription(
    val chatId: ChatId,
    val description: String
) : JsonTgCallable<Boolean>()
