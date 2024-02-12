package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.colors.RgbColor

data class ForumTopicCreated(
    val name: String,
    val iconColor: RgbColor,
    val iconCustomEmojiId: String? = null
)
