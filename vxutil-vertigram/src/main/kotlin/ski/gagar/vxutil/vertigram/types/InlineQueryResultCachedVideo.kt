package ski.gagar.vxutil.vertigram.types

data class InlineQueryResultCachedVideo(
    val id: String,
    val videoFileId: String,
    val title: String,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.VIDEO
}
