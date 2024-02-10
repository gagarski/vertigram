package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.Message

@Throttled
data class ForwardMessages(
    override val chatId: ChatId,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val messageThreadId: Long? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false
) : JsonTelegramCallable<Message>(), HasChatId
