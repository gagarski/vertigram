package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [BusinessLocation](https://core.telegram.org/bots/api#businesslocation) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class BusinessLocation(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val address: String,
    val location: Location? = null,
)