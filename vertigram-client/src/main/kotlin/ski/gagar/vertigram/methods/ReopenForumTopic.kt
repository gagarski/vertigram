package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

data class ReopenForumTopic(
    override val chatId: ChatId,
    val messageThreadId: Long,
) : JsonTelegramCallable<Boolean>(), HasChatId
