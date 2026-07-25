package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change search keywords assigned to a regular or custom emoji sticker.
 *
 * Returns `true` on success.
 *
 * See Telegram's [setStickerKeywords](https://core.telegram.org/bots/api#setstickerkeywords) documentation.
 */
@TelegramCodegen.Method
data class SetStickerKeywords internal constructor(
    /** File identifier of the sticker. */
    val sticker: String,
    /** Zero to 20 search keywords for the sticker, with up to 64 total characters. */
    val keywords: List<String>? = null
) : MultipartTelegramCallable<Boolean>()
