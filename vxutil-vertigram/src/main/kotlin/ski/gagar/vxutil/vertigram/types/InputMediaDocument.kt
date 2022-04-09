package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment

/**
 * Telegram type InputMediaDocument
 */
data class InputMediaDocument(
    override val media: Attachment,
    override val thumb: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableContentTypeDetection: Boolean = false
) : InputMedia() {
    override val type: InputMediaType = InputMediaType.DOCUMENT
    override fun instantiate(media: Attachment, thumb: Attachment?) = copy(media = media, thumb = thumb)
}

val InputMediaDocument.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.caption) } ?: listOf()
