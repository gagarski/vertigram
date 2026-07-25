package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Describes a Web App.
 *
 * See Telegram's [WebAppInfo](https://core.telegram.org/bots/api#webappinfo) documentation.
 */
@TelegramCodegen.Type
data class WebAppInfo internal constructor(
    /** HTTPS URL of the Web App. */
    val url: String
) {
    companion object
}
