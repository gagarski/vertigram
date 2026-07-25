package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the bot's description. Returns `true` on success.
 *
 * See Telegram's [setMyDescription](https://core.telegram.org/bots/api#setmydescription) documentation.
 */
@TelegramCodegen.Method
data class SetMyDescription internal constructor(
    /** New bot description, 0-512 characters. */
    val description: String,
    /** Two-letter ISO 639-1 language code or an empty string. */
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
