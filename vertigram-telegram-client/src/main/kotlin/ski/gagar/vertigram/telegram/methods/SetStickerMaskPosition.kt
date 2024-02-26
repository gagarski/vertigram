package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerMaskPosition](https://core.telegram.org/bots/api#setstickermaskposition) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetStickerMaskPosition(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val sticker: String,
    val maskPosition: Sticker.MaskPosition? = null
) : MultipartTelegramCallable<Boolean>()
