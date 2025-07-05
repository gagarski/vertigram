package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotCommand

/**
 * Telegram [deleteMyCommands](https://core.telegram.org/bots/api#deletemycommands) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeleteMyCommands internal constructor(
    val scope: BotCommand.Scope? = null,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
