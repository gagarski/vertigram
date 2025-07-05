package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMyName](https://core.telegram.org/bots/api#setmyname) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetMyName internal constructor(
    val name: String,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
