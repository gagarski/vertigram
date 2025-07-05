package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [deleteStickerSet](https://core.telegram.org/bots/api#deletestickerset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeleteStickerSet internal constructor(
    val name: String
) : MultipartTelegramCallable<Boolean>()
