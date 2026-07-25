package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.StickerSet
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get a sticker set.
 *
 * See Telegram's [getStickerSet](https://core.telegram.org/bots/api#getstickerset) documentation.
 */
@TelegramCodegen.Method
data class GetStickerSet internal constructor(
    /** Name of the sticker set. */
    val name: String
) : JsonTelegramCallable<StickerSet>()
