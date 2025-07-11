package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerPositionInSet](https://core.telegram.org/bots/api#setstickerpositioninset) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetStickerPositionInSet internal constructor(
    val sticker: String,
    val position: Int
) : JsonTelegramCallable<Boolean>()

