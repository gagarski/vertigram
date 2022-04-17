package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.BotCommand
import ski.gagar.vxutil.vertigram.types.BotCommandScope
import ski.gagar.vxutil.vertigram.types.BotCommandScopeDefault
import ski.gagar.vxutil.vertigram.types.Chat
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class GetMyCommands(
    val scope: BotCommandScope = BotCommandScopeDefault,
    val languageCode: String? = null
) : JsonTgCallable<Boolean>
