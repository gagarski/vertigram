package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.Message

@Throttled
data class ForwardMessage(
    override val chatId: ChatId,
    val fromChatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    // Since Telegram Bot Api 6.3
    val messageThreadId: Long? = null
) : JsonTelegramCallable<Message>(), HasChatId
