package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.BotDescription

@TgMethod
data class GetMyDescription(
    val languageCode: String? = null
) : JsonTgCallable<BotDescription>()
