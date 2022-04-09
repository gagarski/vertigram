package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.Chat
import ski.gagar.vxutil.vertigram.types.ChatId

data class GetChat(
    val chatId: ChatId
) : JsonTgCallable<Chat>()
