package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the mask position of a mask sticker. Returns `true` on success.
 *
 * See Telegram's [setStickerMaskPosition](https://core.telegram.org/bots/api#setstickermaskposition) documentation.
 */
@TelegramCodegen.Method
data class SetStickerMaskPosition internal constructor(
    /** File identifier of the sticker. */
    val sticker: String,
    /** New mask position of the sticker. */
    val maskPosition: Sticker.MaskPosition? = null
) : MultipartTelegramCallable<Boolean>()
