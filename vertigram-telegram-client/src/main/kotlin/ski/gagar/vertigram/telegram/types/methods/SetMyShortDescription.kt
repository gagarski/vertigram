package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the bot's short description. Returns `true` on success.
 *
 * See Telegram's [setMyShortDescription](https://core.telegram.org/bots/api#setmyshortdescription) documentation.
 */
@TelegramCodegen.Method
data class SetMyShortDescription internal constructor(
    /** New short description for the bot, 0-120 characters. */
    val shortDescription: String,
    /** Two-letter ISO 639-1 language code or an empty string. */
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
