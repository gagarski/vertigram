package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [uploadStickerFile](https://core.telegram.org/bots/api#uploadstickerfile) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class UploadStickerFile internal constructor(
    val userId: Long,
    @TelegramMedia
    val sticker: Sticker,
    val stickerFormat: Sticker.Format
) : MultipartTelegramCallable<File>()
