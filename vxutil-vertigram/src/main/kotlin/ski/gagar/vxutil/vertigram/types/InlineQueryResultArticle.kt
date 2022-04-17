package ski.gagar.vxutil.vertigram.types

data class InlineQueryResultArticle(
    val id: String,
    val title: String,
    val inputMessageContent: InputMessageContent,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val url: String? = null,
    val hideUrl: Boolean = false,
    val description: String? = null,
    val thumbUrl: String? = null,
    val thumbWidth: Int? = null,
    val thumbHeight: Int? = null
) : InlineQueryResult {
    override val type = InlineQueryResultType.ARTICLE
}
