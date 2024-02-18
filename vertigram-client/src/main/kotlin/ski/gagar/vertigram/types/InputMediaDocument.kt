package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.types.richtext.HasOptionalRichCaption

data class InputMediaDocument(
    override val media: Attachment,
    override val thumbnail: Attachment? = null,
    override val caption: String? = null,
    override val parseMode: ParseMode? = null,
    override val captionEntities: List<MessageEntity>? = null,
    val disableContentTypeDetection: Boolean = false
) : InputMedia, HasOptionalRichCaption {
    override val type: InputMediaType = InputMediaType.DOCUMENT
    override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)
}
