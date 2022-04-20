package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.BotCommand
import ski.gagar.vxutil.vertigram.types.BotCommandScope
import ski.gagar.vxutil.vertigram.types.BotCommandScopeDefault

@TgMethod
data class SetMyCommands(
    val commands: List<BotCommand>,
    val scope: BotCommandScope = BotCommandScopeDefault,
    val languageCode: String? = null
) : JsonTgCallable<Boolean>()
