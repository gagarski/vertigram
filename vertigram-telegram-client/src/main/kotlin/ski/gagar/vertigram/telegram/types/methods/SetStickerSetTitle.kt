package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to set the title of a created sticker set. Returns `true` on success.
 *
 * See Telegram's [setStickerSetTitle](https://core.telegram.org/bots/api#setstickersettitle) documentation.
 */
@TelegramCodegen.Method
data class SetStickerSetTitle internal constructor(
    /** Sticker set name. */
    val name: String,
    /** Sticker set title, 1-64 characters. */
    val title: String
) : MultipartTelegramCallable<Boolean>()
