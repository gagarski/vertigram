package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Contains information about the location of a Telegram Business account.
 *
 * See Telegram's [BusinessLocation](https://core.telegram.org/bots/api#businesslocation) documentation.
 */
@TelegramCodegen.Type
data class BusinessLocation internal constructor(
    /** Address of the business. */
    val address: String,
    /** Location of the business. */
    val location: Location? = null,
) {
    companion object
}
