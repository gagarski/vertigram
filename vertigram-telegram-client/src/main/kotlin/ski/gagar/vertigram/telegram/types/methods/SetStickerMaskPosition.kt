package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerMaskPosition](https://core.telegram.org/bots/api#setstickermaskposition) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetStickerMaskPosition internal constructor(
    val sticker: String,
    val maskPosition: Sticker.MaskPosition? = null
) : MultipartTelegramCallable<Boolean>()
