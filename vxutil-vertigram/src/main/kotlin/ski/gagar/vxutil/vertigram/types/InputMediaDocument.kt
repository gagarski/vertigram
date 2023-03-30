package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment

data class InputMediaDocument(
    override val media: Attachment,
    override val thumbnail: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val disableContentTypeDetection: Boolean = false
) : InputMedia {
    override val type: InputMediaType = InputMediaType.DOCUMENT
    override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)
}
