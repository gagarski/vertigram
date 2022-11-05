package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class EditForumTopic(
    val chatId: ChatId,
    val messageThreadId: Long,
    val name: String,
    val iconCustomEmojiId: String? = null
) : JsonTgCallable<Boolean>()
