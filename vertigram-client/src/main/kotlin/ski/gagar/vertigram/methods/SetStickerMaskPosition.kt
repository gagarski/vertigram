package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.MaskPosition
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
    val maskPosition: MaskPosition? = null
) : MultipartTelegramCallable<Boolean>()
