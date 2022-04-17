package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.MenuButton

@TgMethod
data class GetChatMenuButton(
    val chatId: Long? = null
) : JsonTgCallable<Boolean>
