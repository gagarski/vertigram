package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
@Throttled
data class ForwardMessage(
    override val chatId: ChatId,
    val fromChatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    // Since Telegram Bot Api 6.3
    val messageThreadId: Long? = null
) : JsonTgCallable<Message>(), HasChatId
