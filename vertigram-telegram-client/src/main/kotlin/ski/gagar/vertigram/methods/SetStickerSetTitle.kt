package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerSetTitle](https://core.telegram.org/bots/api#setstickersettitle) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetStickerSetTitle(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val name: String,
    val title: String
) : MultipartTelegramCallable<Boolean>()
