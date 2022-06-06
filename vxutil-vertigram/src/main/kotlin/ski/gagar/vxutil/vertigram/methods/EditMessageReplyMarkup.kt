package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.Message

@TgMethod
data class EditMessageReplyMarkup(
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null
) : JsonTgCallable<Message>()
