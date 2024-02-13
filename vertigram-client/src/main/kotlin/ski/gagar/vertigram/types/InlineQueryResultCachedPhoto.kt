package ski.gagar.vertigram.types

data class InlineQueryResultCachedPhoto(
    val id: String,
    val photoFileId: String,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.PHOTO
}
