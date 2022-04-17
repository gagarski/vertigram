package ski.gagar.vxutil.vertigram.types

data class InlineQueryResultPhoto(
    val id: String,
    val photoUrl: String,
    val thumbUrl: String,
    val photoWidth: Int? = null,
    val photoHeight: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null

) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.PHOTO
}

val InlineQueryResultPhoto.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
