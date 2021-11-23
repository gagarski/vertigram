package ski.gagar.vxutil.vertigram.entities.requests

import ski.gagar.vxutil.vertigram.entities.ChatMember

data class GetChatMember(val chatId: Long, val userId: Long) : JsonTgCallable<ChatMember>()
