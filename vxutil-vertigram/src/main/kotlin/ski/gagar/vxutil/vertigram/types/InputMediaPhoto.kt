package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InputMediaPhoto.
 */
data class InputMediaPhoto(
    val media: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.PHOTO
}

val InputMediaPhoto.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
