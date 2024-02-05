package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.BotCommand
import ski.gagar.vertigram.types.BotCommandScope
import ski.gagar.vertigram.types.BotCommandScopeDefault

@TgMethod
data class SetMyCommands(
    val commands: List<BotCommand>,
    val scope: BotCommandScope = BotCommandScopeDefault,
    val languageCode: String? = null
) : JsonTgCallable<Boolean>()
