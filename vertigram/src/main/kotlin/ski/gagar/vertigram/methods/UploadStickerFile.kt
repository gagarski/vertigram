package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.Sticker
import ski.gagar.vertigram.types.StickerFormat
import ski.gagar.vertigram.util.multipart.TgMedia

@TgMethod
data class UploadStickerFile(
    val userId: Long,
    @TgMedia
    val sticker: Sticker,
    val stickerFormat: StickerFormat
) : MultipartTgCallable<Message>()
