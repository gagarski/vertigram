package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatIdLong

@TgMethod
data class GetChatMenuButton(
    override val chatId: Long? = null
) : JsonTgCallable<Boolean>(), HasChatIdLong
