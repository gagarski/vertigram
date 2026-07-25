package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to move a sticker in a set created by the bot to a specific position.
 *
 * Returns `true` on success.
 *
 * See Telegram's
 * [setStickerPositionInSet](https://core.telegram.org/bots/api#setstickerpositioninset) documentation.
 */
@TelegramCodegen.Method
data class SetStickerPositionInSet internal constructor(
    /** File identifier of the sticker. */
    val sticker: String,
    /** Zero-based new position of the sticker in the set. */
    val position: Int
) : JsonTelegramCallable<Boolean>()

