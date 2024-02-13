package ski.gagar.vertigram.types

data class InlineQueryResultCachedVoice(
    val id: String,
    val voiceFileId: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.VOICE
}
