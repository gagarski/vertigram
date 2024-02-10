package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId

@Throttled
data class UnpinChatMessage(
    override val chatId: ChatId,
    val messageId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
