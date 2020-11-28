package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.Message
import ski.gagar.vertigram.entities.ParseMode
import ski.gagar.vertigram.entities.ReplyMarkup

data class SendMessage(
    val chatId: Long,
    val text: String,
    val parseMode: ParseMode? = null,
    val disableWebPagePreview: Boolean = false,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()