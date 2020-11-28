package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.Message

data class ForwardMessage(
    val chatId: Long,
    val fromChatId: Long,
    val disableNotification: Boolean = false, // Optional by specification. By default sent as false
    val messageId: Long
) : JsonTgCallable<Message>()