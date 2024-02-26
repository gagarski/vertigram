package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [WebAppInfo](https://core.telegram.org/bots/api#webappinfo) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class WebAppInfo(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val url: String
)
