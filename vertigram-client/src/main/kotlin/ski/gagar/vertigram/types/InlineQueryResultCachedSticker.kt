package ski.gagar.vertigram.types

data class InlineQueryResultCachedSticker(
    val id: String,
    val stickerFileId: String,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type = InlineQueryResultType.STICKER
}
