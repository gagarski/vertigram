package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

@TgMethod
data class DeleteMessages(
    override val chatId: ChatId,
    val messageIds: List<Long>
) : JsonTgCallable<Boolean>(), HasChatId
