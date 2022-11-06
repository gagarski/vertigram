package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
@Throttled
data class UnpinChatMessage(
    override val chatId: ChatId,
    val messageId: Long
) : JsonTgCallable<Boolean>(), HasChatId
