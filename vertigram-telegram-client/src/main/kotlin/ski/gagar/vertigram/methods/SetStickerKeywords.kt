package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerKeywords](https://core.telegram.org/bots/api#setstickerkeywords) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetStickerKeywords(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val sticker: String,
    val keywords: List<String>? = null
) : MultipartTelegramCallable<Boolean>()
