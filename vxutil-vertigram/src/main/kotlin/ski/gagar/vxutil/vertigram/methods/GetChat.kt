package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Chat
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class GetChat(
    val chatId: ChatId
) : JsonTgCallable<Chat>()
