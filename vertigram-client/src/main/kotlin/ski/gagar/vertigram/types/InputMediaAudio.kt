package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.types.richtext.HasOptionalRichCaption
import java.time.Duration

data class InputMediaAudio(
    override val media: Attachment,
    override val thumbnail: Attachment? = null,
    override val caption: String? = null,
    override val parseMode: ParseMode? = null,
    override val captionEntities: List<MessageEntity>? = null,
    val duration: Duration? = null,
    val performer: String? = null,
    val title: String? = null
) : InputMedia, HasOptionalRichCaption {
    override val type: InputMediaType = InputMediaType.AUDIO
    override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)
}
