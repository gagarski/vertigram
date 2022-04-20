package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.BotCommandScope
import ski.gagar.vxutil.vertigram.types.BotCommandScopeDefault

@TgMethod
data class DeleteMyCommands(
    val scope: BotCommandScope = BotCommandScopeDefault,
    val languageCode: String? = null
) : JsonTgCallable<Boolean>()
