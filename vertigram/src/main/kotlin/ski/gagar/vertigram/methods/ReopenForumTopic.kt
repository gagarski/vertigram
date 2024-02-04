package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

@TgMethod
data class ReopenForumTopic(
    override val chatId: ChatId,
    val messageThreadId: Long,
) : JsonTgCallable<Boolean>(), HasChatId
