package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Telegram [addStickerToSet](https://core.telegram.org/bots/api#addstickertoset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class AddStickerToSet internal constructor(
    val userId: Long,
    val name: String,
    @TelegramMedia
    val sticker: InputMedia.Sticker
) : MultipartTelegramCallable<Boolean>()
