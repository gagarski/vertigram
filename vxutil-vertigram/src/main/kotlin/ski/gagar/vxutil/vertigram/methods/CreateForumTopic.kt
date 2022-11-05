package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ForumTopic
import ski.gagar.vxutil.vertigram.types.TopicColor

@TgMethod
data class CreateForumTopic(
    val chatId: ChatId,
    val name: String,
    val iconColor: TopicColor? = null,
    val iconCustomEmojiId: String? = null
) : JsonTgCallable<ForumTopic>()
