package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.util.TgMethodName

data class EditMessageReplyMarkup(
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null,
    val inlineKeyboardMarkup: InlineKeyboardMarkup? = null
) : JsonTgCallable<Message>()
