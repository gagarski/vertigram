package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMyShortDescription](https://core.telegram.org/bots/api#setmyshortdescription) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetMyShortDescription internal constructor(
    val shortDescription: String,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
