package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

data class SendMessage(
    val chatId: Long,
    val text: String,
    val parseMode: ParseMode? = null,
    val disableWebPagePreview: Boolean = false,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()
