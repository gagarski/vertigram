package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotCommand
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setMyCommands](https://core.telegram.org/bots/api#setmycommands) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetMyCommands internal constructor(
    val commands: List<BotCommand>,
    val scope: BotCommand.Scope? = null,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
