package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Chat
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatMember

@TgMethod
data class GetChatAdministrators(
    val chatId: ChatId
) : JsonTgCallable<List<ChatMember>>
