package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [deleteStickerFromSet](https://core.telegram.org/bots/api#deletestickerfromset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeleteStickerFromSet internal constructor(
    val sticker: String
) : JsonTelegramCallable<Boolean>()

