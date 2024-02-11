package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMyDescription](https://core.telegram.org/bots/api#setmydescription) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetMyDescription(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val description: String,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
