package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

data class UnbanChatSenderChat(
    override val chatId: ChatId,
    val senderChatId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
