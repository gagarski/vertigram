package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatMember

@TgMethod
data class GetChatAdministrators(
    override val chatId: ChatId
) : JsonTgCallable<List<ChatMember>>(), HasChatId
