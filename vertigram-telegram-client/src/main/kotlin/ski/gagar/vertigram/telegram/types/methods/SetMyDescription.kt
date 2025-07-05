package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMyDescription](https://core.telegram.org/bots/api#setmydescription) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetMyDescription internal constructor(
    val description: String,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
