package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultCachedGif.
 */
data class InlineQueryResultCachedGif(
    val id: String,
    val gifFileId: String,
    val title: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.GIF
}

val InlineQueryResultCachedGif.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
