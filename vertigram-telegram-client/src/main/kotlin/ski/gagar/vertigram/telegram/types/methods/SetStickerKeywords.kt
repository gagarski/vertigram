package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerKeywords](https://core.telegram.org/bots/api#setstickerkeywords) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetStickerKeywords internal constructor(
    val sticker: String,
    val keywords: List<String>? = null
) : MultipartTelegramCallable<Boolean>()
