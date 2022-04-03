package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.Message
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
