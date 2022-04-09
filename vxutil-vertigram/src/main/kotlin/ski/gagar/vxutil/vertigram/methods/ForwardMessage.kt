package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message

/**
 * Telegram method forwardMessage
 */
data class ForwardMessage(
    val chatId: ChatId,
    val fromChatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false
) : JsonTgCallable<Message>()
