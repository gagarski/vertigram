package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.BotShortDescription
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyShortDescription](https://core.telegram.org/bots/api#getmyshortdescription) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetMyShortDescription(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val languageCode: String? = null
) : JsonTelegramCallable<BotShortDescription>()
