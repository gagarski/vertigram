package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Use this method to delete a sticker from a set created by the bot. Returns `true` on success.
 *
 * See Telegram's [deleteStickerFromSet](https://core.telegram.org/bots/api#deletestickerfromset) documentation.
 */
@TelegramCodegen.Method
data class DeleteStickerFromSet internal constructor(
    /** File identifier of the sticker. */
    val sticker: String
) : JsonTelegramCallable<Boolean>()

