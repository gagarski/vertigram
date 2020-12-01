package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.InlineKeyboardMarkup
import ski.gagar.vertigram.entities.Message
import ski.gagar.vertigram.util.TgMethodName

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
