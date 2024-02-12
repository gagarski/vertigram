package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.colors.RgbColor

data class ForumTopic(
    val messageThreadId: Long,
    val name: String,
    val iconColor: RgbColor,
    val iconCustomEmojiId: String? = null
)
