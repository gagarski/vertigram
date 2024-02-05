package ski.gagar.vertigram.types

data class ForumTopicCreated(
    val name: String,
    val iconColor: RgbColor,
    val iconCustomEmojiId: String? = null
)
