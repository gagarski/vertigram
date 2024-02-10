package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.Sticker
import ski.gagar.vertigram.types.StickerFormat
import ski.gagar.vertigram.annotations.TelegramMedia

data class UploadStickerFile(
    val userId: Long,
    @TelegramMedia
    val sticker: Sticker,
    val stickerFormat: StickerFormat
) : MultipartTelegramCallable<Message>()
