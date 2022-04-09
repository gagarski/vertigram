package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatMember

data class GetChatMember(val chatId: ChatId, val userId: Long) : JsonTgCallable<ChatMember>()
