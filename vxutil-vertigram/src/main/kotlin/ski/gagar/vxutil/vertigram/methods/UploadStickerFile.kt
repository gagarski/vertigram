package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.Sticker
import ski.gagar.vxutil.vertigram.types.StickerFormat
import ski.gagar.vxutil.vertigram.util.multipart.TgMedia

@TgMethod
data class UploadStickerFile(
    val userId: Long,
    @TgMedia
    val sticker: Sticker,
    val stickerFormat: StickerFormat
) : MultipartTgCallable<Message>()
