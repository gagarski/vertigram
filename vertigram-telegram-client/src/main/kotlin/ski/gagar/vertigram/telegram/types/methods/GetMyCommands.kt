package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotCommand
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getMyCommands](https://core.telegram.org/bots/api#getmycommands) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetMyCommands internal constructor(
    val scope: BotCommand.Scope? = null,
    val languageCode: String? = null
) : JsonTelegramCallable<List<BotCommand>>()