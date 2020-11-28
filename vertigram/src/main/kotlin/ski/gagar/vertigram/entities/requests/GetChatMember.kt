package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.ChatMember

data class GetChatMember(val chatId: Long, val userId: Long) : JsonTgCallable<ChatMember>()