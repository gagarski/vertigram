package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultCachedMpeg4Gif.
 */
data class InlineQueryResultCachedMpeg4Gif(
    val id: String,
    val mpeg4FileId: String,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.MPEG4_GIF
}

val InlineQueryResultCachedMpeg4Gif.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
