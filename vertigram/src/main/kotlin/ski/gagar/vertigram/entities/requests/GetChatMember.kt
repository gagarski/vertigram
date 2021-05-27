package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.entities.ChatMember

@TgMethod
data class GetChatMember(val chatId: Long, val userId: Long) : JsonTgCallable<ChatMember>()
