package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatMember

data class GetChatMember(val chatId: Long, val userId: Long) : JsonTgCallable<ChatMember>()
