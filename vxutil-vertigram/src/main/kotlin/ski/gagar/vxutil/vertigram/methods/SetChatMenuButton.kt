package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.MenuButton

@TgMethod
data class SetChatMenuButton(
    val chatId: Long? = null,
    val menuButton: MenuButton? = null
) : JsonTgCallable<Boolean>
