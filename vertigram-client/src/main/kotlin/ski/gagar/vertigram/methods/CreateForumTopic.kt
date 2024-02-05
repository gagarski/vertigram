package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ForumTopic
import ski.gagar.vertigram.types.ForumTopicColor

@TgMethod
@Throttled
data class CreateForumTopic(
    override val chatId: ChatId,
    val name: String,
    val iconColor: ForumTopicColor? = null,
    val iconCustomEmojiId: String? = null
) : JsonTgCallable<ForumTopic>(), HasChatId
