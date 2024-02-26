package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.Sticker
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getCustomEmojiStickers](https://core.telegram.org/bots/api#getcustomemojistickers) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetCustomEmojiStickers(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val customEmojiIds: List<String>
) : JsonTelegramCallable<List<Sticker>>()
