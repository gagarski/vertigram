package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatIdLong
import ski.gagar.vxutil.vertigram.types.MenuButton

@TgMethod
data class SetChatMenuButton(
    override val chatId: Long? = null,
    val menuButton: MenuButton? = null
) : JsonTgCallable<Boolean>(), HasChatIdLong
