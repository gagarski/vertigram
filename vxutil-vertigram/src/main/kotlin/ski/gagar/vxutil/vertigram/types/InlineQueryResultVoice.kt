package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InlineQueryResultVoice.
 */
data class InlineQueryResultVoice(
    val id: String,
    val voiceUrl: String,
    val title: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val voiceDuration: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult() {
    override val type: InlineQueryResultType = InlineQueryResultType.VOICE
}

val InlineQueryResultVoice.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
