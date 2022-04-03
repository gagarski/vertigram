package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultCachedVideo.
 */
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
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.VIDEO
}

val InlineQueryResultCachedVideo.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
