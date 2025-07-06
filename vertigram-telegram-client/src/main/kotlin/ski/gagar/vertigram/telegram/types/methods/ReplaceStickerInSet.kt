package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Telegram [replaceStickerInSet](https://core.telegram.org/bots/api#replacestickerinset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ReplaceStickerInSet internal constructor(
    val userId: Long,
    val name: String,
    val oldSticker: String,
    val sticker: InputMedia.Sticker
) : MultipartTelegramCallable<Boolean>()
