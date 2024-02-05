package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatIdLong
import ski.gagar.vertigram.types.MenuButton

@TgMethod
data class SetChatMenuButton(
    override val chatId: Long? = null,
    val menuButton: MenuButton? = null
) : JsonTgCallable<Boolean>(), HasChatIdLong
