package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get information about custom emoji stickers by their identifiers.
 *
 * See Telegram's [getCustomEmojiStickers](https://core.telegram.org/bots/api#getcustomemojistickers) documentation.
 */
@TelegramCodegen.Method
data class GetCustomEmojiStickers internal constructor(
    /** List of custom emoji identifiers; at most 200 identifiers can be specified. */
    val customEmojiIds: List<String>
) : JsonTelegramCallable<List<Sticker>>()
