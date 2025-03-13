package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotName
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyName](https://core.telegram.org/bots/api#getmyname) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class GetMyName(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val languageCode: String? = null
) : JsonTelegramCallable<BotName>()
