package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
@Throttled
data class CopyMessages(
    override val chatId: ChatId,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val messageThreadId: Long? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val removeCaption: Boolean = false
) : JsonTgCallable<Message>(), HasChatId
