package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId

@TgMethod
@Throttled
data class PinChatMessage(
    override val chatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false
) : JsonTgCallable<Boolean>(), HasChatId
