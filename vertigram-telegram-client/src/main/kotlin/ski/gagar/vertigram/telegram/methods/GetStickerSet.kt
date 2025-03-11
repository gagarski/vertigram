package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.StickerSet
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getStickerSet](https://core.telegram.org/bots/api#getstickerset) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class GetStickerSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val name: String
) : JsonTelegramCallable<StickerSet>()
