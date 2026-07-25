package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotDescription
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get the current bot description for the given user language.
 *
 * See Telegram's [getMyDescription](https://core.telegram.org/bots/api#getmydescription) documentation.
 */
@TelegramCodegen.Method
data class GetMyDescription internal constructor(
    /** Two-letter ISO 639-1 language code or an empty string. */
    val languageCode: String? = null
) : JsonTelegramCallable<BotDescription>()
