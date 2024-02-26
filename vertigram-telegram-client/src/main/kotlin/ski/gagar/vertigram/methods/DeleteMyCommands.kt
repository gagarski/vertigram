package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.BotCommand
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [deleteMyCommands](https://core.telegram.org/bots/api#deletemycommands) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class DeleteMyCommands(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val scope: BotCommand.Scope? = null,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
