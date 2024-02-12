package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ChatLocation](https://core.telegram.org/bots/api#chatlocation) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ChatLocation(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val location: Location,
    val address: String
)
