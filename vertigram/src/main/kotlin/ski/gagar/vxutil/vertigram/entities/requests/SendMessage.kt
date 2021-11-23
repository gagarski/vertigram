package ski.gagar.vxutil.vertigram.entities.requests

import ski.gagar.vxutil.vertigram.entities.Message
import ski.gagar.vxutil.vertigram.entities.ParseMode
import ski.gagar.vxutil.vertigram.entities.ReplyMarkup

data class SendMessage(
    val chatId: Long,
    val text: String,
    val parseMode: ParseMode? = null,
    val disableWebPagePreview: Boolean = false,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()
