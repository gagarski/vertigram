package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
@Throttled
data class SetChatDescription(
    override val chatId: ChatId,
    val description: String
) : JsonTgCallable<Boolean>(), HasChatId
