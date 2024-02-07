package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [deleteStickerFromSet](https://core.telegram.org/bots/api#deletestickerfromset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgMethod
data class DeleteStickerFromSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val sticker: String
) : JsonTgCallable<Boolean>()

