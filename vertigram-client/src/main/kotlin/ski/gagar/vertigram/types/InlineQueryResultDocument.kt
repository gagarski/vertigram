package ski.gagar.vertigram.types

data class InlineQueryResultDocument(
    val id: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val documentUrl: String,
    val mimeType: String,
    val description: String? = null,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Int? = null,
    val thumbnailHeight: Int? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.DOCUMENT
}
