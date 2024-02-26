package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setStickerPositionInSet](https://core.telegram.org/bots/api#setstickerpositioninset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetStickerPositionInSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val sticker: String,
    val position: Int
) : JsonTelegramCallable<Boolean>()

