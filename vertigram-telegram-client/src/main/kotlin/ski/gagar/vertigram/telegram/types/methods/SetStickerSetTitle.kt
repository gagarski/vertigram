package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerSetTitle](https://core.telegram.org/bots/api#setstickersettitle) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetStickerSetTitle internal constructor(
    val name: String,
    val title: String
) : MultipartTelegramCallable<Boolean>()
