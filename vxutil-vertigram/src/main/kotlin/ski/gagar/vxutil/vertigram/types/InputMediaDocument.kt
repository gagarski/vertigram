package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type InputMediaDocument
 */
data class InputMediaDocument(
    val media: String,
    val thumb: String,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableContentTypeDetection: Boolean = false
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.DOCUMENT
}

val InputMediaDocument.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
