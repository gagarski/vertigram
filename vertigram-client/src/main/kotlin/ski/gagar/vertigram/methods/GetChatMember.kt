package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatMember

@TgMethod
data class GetChatMember(
    override val chatId: ChatId,
    val userId: Long
) : JsonTgCallable<ChatMember>(), HasChatId
