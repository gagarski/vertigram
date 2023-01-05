package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import java.time.Duration

data class InputMediaVideo(
    override val media: Attachment,
    override val thumb: Attachment? = null,
    val caption: String? = null,
    val parseMode: ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val width: Int? = null,
    val height: Int? = null,
    val duration: Duration? = null,
    val supportsStreaming: Boolean = false,
    // Since Telegram Bot API 6.4
    val hasSpoiler: Boolean = false
) : InputMedia {
    override val type: InputMediaType = InputMediaType.VIDEO
    override fun instantiate(media: Attachment, thumb: Attachment?) = copy(media = media, thumb = thumb)
}
