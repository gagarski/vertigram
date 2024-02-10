package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

data class LeaveChat(
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
