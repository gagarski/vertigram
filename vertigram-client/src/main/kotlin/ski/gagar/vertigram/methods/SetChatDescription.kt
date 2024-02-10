package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId

@Throttled
data class SetChatDescription(
    override val chatId: ChatId,
    val description: String
) : JsonTelegramCallable<Boolean>(), HasChatId
