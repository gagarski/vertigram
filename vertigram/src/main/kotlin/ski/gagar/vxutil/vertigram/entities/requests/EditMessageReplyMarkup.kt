package ski.gagar.vxutil.vertigram.entities.requests

import ski.gagar.vxutil.vertigram.entities.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.entities.Message
import ski.gagar.vxutil.vertigram.util.TgMethodName

data class EditMessageReplyMarkup(
    val chatId: Long,
    val messageId: Long,
    val inlineKeyboardMarkup: InlineKeyboardMarkup? = null
) : JsonTgCallable<Message>()

@TgMethodName("editMessageReplyMarkup")
data class EditMessageReplyMarkupInline(
    val inlineMessageId: Long,
    val inlineKeyboardMarkup: InlineKeyboardMarkup? = null
) : JsonTgCallable<Boolean>()
