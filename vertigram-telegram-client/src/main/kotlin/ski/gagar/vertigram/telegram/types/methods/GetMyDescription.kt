package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotDescription
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyDescription](https://core.telegram.org/bots/api#getmydescription) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetMyDescription internal constructor(
    val languageCode: String? = null
) : JsonTelegramCallable<BotDescription>()
