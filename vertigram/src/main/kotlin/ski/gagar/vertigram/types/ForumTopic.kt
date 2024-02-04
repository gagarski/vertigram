package ski.gagar.vertigram.types

data class ForumTopic(
    val messageThreadId: Long,
    val name: String,
    val iconColor: RgbColor,
    val iconCustomEmojiId: String? = null
)
