package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultGif.
 */
data class InlineQueryResultGif(
    val id: String,
    val gifUrl: String,
    val gifWidth: Long? = null,
    val gifHeight: Long? = null,
    val gifDuration: Long? = null,
    val thumbUrl: String,
    val thumbMimeType: String? = null,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.GIF
}

val InlineQueryResultGif.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
