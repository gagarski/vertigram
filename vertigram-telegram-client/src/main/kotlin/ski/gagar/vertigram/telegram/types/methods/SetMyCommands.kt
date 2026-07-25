package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotCommand
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the list of the bot's commands. Returns `true` on success.
 *
 * See Telegram's [setMyCommands](https://core.telegram.org/bots/api#setmycommands) documentation.
 */
@TelegramCodegen.Method
data class SetMyCommands internal constructor(
    /** Bot commands to set; at most 100 commands can be specified. */
    val commands: List<BotCommand>,
    /** Scope of users for which the commands are relevant. */
    val scope: BotCommand.Scope? = null,
    /** Two-letter ISO 639-1 language code or an empty string. */
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
