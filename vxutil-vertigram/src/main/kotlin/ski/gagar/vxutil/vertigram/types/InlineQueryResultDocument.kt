package ski.gagar.vxutil.vertigram.types

data class InlineQueryResultDocument(
    val id: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val documentUrl: String,
    val mimeType: String,
    val description: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbUrl: String? = null,
    val thumbWidth: Int? = null,
    val thumbHeight: Int? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.DOCUMENT
}
