package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatMember

data class GetChatAdministrators(
    override val chatId: ChatId
) : JsonTelegramCallable<List<ChatMember>>(), HasChatId
