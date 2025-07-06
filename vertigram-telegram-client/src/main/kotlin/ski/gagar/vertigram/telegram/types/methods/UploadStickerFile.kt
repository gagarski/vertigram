package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Telegram [uploadStickerFile](https://core.telegram.org/bots/api#uploadstickerfile) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class UploadStickerFile internal constructor(
    val userId: Long,
    val sticker: Sticker,
    val stickerFormat: Sticker.Format
) : MultipartTelegramCallable<File>()
