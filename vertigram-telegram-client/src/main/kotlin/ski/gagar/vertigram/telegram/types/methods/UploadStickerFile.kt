package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.File
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Use this method to upload a file with a sticker for later use in
 * [ski.gagar.vertigram.telegram.client.Telegram.createNewStickerSet] and
 * [ski.gagar.vertigram.telegram.client.Telegram.addStickerToSet].
 *
 * See Telegram's [uploadStickerFile](https://core.telegram.org/bots/api#uploadstickerfile) documentation.
 */
@TelegramCodegen.Method
data class UploadStickerFile internal constructor(
    /** User identifier of the sticker file owner. */
    val userId: Long,
    /** Sticker file to upload. */
    val sticker: Sticker,
    /** Format of the sticker. */
    val stickerFormat: Sticker.Format
) : MultipartTelegramCallable<File>()
