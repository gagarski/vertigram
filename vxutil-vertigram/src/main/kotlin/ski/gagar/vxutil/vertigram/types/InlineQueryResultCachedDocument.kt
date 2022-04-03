package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultCachedDocument.
 */
data class InlineQueryResultCachedDocument(
    val id: String,
    val title: String,
    val documentFileId: String,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.DOCUMENT
}

val InlineQueryResultCachedDocument.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
