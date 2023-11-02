package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class SetMyName(
    val name: String,
    val languageCode: String? = null
) : JsonTgCallable<Boolean>()
