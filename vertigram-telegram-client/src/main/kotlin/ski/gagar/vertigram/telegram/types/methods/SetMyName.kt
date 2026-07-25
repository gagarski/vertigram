package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the bot's name. Returns `true` on success.
 *
 * See Telegram's [setMyName](https://core.telegram.org/bots/api#setmyname) documentation.
 */
@TelegramCodegen.Method
data class SetMyName internal constructor(
    /** New bot name, 0-64 characters. */
    val name: String,
    /** Two-letter ISO 639-1 language code or an empty string. */
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
