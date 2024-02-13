package ski.gagar.vertigram.types

data class InlineQueryResultArticle(
    val id: String,
    val title: String,
    val inputMessageContent: InputMessageContent,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
    val url: String? = null,
    val hideUrl: Boolean = false,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Int? = null,
    val thumbnailHeight: Int? = null
) : InlineQueryResult {
    override val type = InlineQueryResultType.ARTICLE
}
