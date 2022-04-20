package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class GetChatMenuButton(
    val chatId: Long? = null
) : JsonTgCallable<Boolean>()
