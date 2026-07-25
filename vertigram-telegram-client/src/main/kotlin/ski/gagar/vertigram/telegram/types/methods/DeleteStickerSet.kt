package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Use this method to delete a sticker set that was created by the bot. Returns `true` on success.
 *
 * See Telegram's [deleteStickerSet](https://core.telegram.org/bots/api#deletestickerset) documentation.
 */
@TelegramCodegen.Method
data class DeleteStickerSet internal constructor(
    /** Sticker set name. */
    val name: String
) : MultipartTelegramCallable<Boolean>()
