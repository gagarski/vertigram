package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatMember

@TgMethod
data class GetChatMember(
    override val chatId: ChatId,
    val userId: Long
) : JsonTgCallable<ChatMember>(), HasChatId
