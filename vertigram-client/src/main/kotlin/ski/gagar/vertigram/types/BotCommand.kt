package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [BotCommand](https://core.telegram.org/bots/api#botcommand) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class BotCommand(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val command: String,
    val description: String
)
