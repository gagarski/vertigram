package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [BusinessLocation](https://core.telegram.org/bots/api#businesslocation) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BusinessLocation internal constructor(
    val address: String,
    val location: Location? = null,
) {
    companion object
}