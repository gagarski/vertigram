package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

@TgMethod
data class EditMessageText(
    val text: String,
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageid: Long? = null,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val disableWebPagePreview: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()
