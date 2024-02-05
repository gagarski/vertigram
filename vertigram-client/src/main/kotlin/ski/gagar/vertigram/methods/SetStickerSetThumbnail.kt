package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.util.multipart.TgMedia

@TgMethod
data class SetStickerSetThumbnail(
    val name: String,
    val userId: Long,
    @TgMedia
    val thumbnail: Attachment
) : MultipartTgCallable<Message>()
