package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatAction
import ski.gagar.vertigram.types.ChatId

@Throttled
data class SendChatAction(
    override val chatId: ChatId,
    val action: ChatAction,
    // Since Telegram Bot API 6.4
    val messageThreadId: Long? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
