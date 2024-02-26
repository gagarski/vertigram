package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.types.BotDescription
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyDescription](https://core.telegram.org/bots/api#getmydescription) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetMyDescription(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val languageCode: String? = null
) : JsonTelegramCallable<BotDescription>()
