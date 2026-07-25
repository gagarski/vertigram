package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotCommand

/**
 * Use this method to delete the list of the bot's commands for the given scope and user language. After deletion,
 * [higher-level commands](https://core.telegram.org/bots/api#determining-list-of-commands) will be shown to affected
 * users. Returns `true` on success.
 *
 * See Telegram's [deleteMyCommands](https://core.telegram.org/bots/api#deletemycommands) documentation.
 */
@TelegramCodegen.Method
data class DeleteMyCommands internal constructor(
    /** Scope of users for which the commands are relevant. */
    val scope: BotCommand.Scope? = null,
    /**
     * Two-letter ISO 639-1 language code. If empty, commands will be applied to all users from [scope] for whose
     * language there are no dedicated commands.
     */
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
