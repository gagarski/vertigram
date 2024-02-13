package ski.gagar.vertigram.types

data class InlineQueryResultCachedMpeg4Gif(
    val id: String,
    val mpeg4FileId: String,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.MPEG4_GIF
}
