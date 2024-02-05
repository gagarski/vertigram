package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

@TgMethod
data class BanChatSenderChat(
    override val chatId: ChatId,
    val senderChatId: Long
) : JsonTgCallable<Boolean>(), HasChatId
