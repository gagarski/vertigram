package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotCommand
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language.
 *
 * See Telegram's [getMyCommands](https://core.telegram.org/bots/api#getmycommands) documentation.
 */
@TelegramCodegen.Method
data class GetMyCommands internal constructor(
    /** Scope of users for which the commands are relevant. */
    val scope: BotCommand.Scope? = null,
    /** Two-letter ISO 639-1 language code or an empty string. */
    val languageCode: String? = null
) : JsonTelegramCallable<List<BotCommand>>()
