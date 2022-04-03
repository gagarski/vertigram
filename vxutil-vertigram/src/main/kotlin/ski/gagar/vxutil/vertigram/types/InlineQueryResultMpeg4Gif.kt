package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultMpeg4Gif.
 */
data class InlineQueryResultMpeg4Gif(
    val id: String,
    val mpeg4Url: String,
    val mpeg4Width: Long? = null,
    val mpeg4Height: Long? = null,
    val mpeg4Duration: Long? = null,
    val thumbUrl: String,
    val thumbMimeType: String? = null,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.MPEG4_GIF
}

val InlineQueryResultMpeg4Gif.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
