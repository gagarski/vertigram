package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

data class GetChatMemberCount(
    override val chatId: ChatId
) : JsonTelegramCallable<Int>(), HasChatId
