package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia

@TgMethod
data class UploadStickerFile(
    val userId: Long,
    @TgMedia
    val pngSticker: Attachment
) : MultipartTgCallable<Message>()
