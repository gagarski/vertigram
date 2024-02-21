package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.types.File
import ski.gagar.vertigram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [uploadStickerFile](https://core.telegram.org/bots/api#uploadstickerfile) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class UploadStickerFile(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    @TelegramMedia
    val sticker: Sticker,
    val stickerFormat: Sticker.Format
) : MultipartTelegramCallable<File>()
