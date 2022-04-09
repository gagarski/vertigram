package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

data class StopMessageLiveLocation(
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()
