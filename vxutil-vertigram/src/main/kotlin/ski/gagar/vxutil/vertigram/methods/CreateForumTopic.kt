package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ForumTopic
import ski.gagar.vxutil.vertigram.types.ForumTopicColor

@TgMethod
@Throttled
data class CreateForumTopic(
    override val chatId: ChatId,
    val name: String,
    val iconColor: ForumTopicColor? = null,
    val iconCustomEmojiId: String? = null
) : JsonTgCallable<ForumTopic>(), HasChatId
