package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId

@TgMethod
@Throttled
data class EditForumTopic(
    override val chatId: ChatId,
    val messageThreadId: Long,
    val name: String? = null,
    val iconCustomEmojiId: String? = null
) : JsonTgCallable<Boolean>(), HasChatId
