package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatIdLong

@TgMethod
data class GetChatMenuButton(
    override val chatId: Long? = null
) : JsonTgCallable<Boolean>(), HasChatIdLong
