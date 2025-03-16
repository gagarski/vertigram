package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotShortDescription
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyShortDescription](https://core.telegram.org/bots/api#getmyshortdescription) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetMyShortDescription internal constructor(
    val languageCode: String? = null
) : JsonTelegramCallable<BotShortDescription>()
