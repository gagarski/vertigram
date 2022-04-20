package ski.gagar.vxutil.vertigram.types

data class InlineQueryResultCachedAudio(
    val id: String,
    val audioFileId: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.AUDIO
}
