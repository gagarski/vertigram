package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMyName](https://core.telegram.org/bots/api#setmyname) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetMyName(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val name: String,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
