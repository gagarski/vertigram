package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.BotCommandScope
import ski.gagar.vertigram.types.BotCommandScopeDefault

data class GetMyCommands(
    val scope: BotCommandScope = BotCommandScopeDefault,
    val languageCode: String? = null
) : JsonTelegramCallable<Boolean>()
