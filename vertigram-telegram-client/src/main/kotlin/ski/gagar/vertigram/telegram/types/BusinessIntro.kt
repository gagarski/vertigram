package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Contains information about the start page settings of a Telegram Business account.
 *
 * See Telegram's [BusinessIntro](https://core.telegram.org/bots/api#businessintro) documentation.
 */
@TelegramCodegen.Type
data class BusinessIntro internal constructor(
    /** Title text of the business intro. */
    val title: String? = null,
    /** Message text of the business intro. */
    val message: String? = null,
    /** Sticker of the business intro. */
    val sticker: Sticker? = null
) {
    companion object
}
