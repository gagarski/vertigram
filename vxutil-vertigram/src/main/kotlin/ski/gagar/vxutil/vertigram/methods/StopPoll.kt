package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

data class StopPoll(
    val chatId: ChatId,
    val messageId: Long,
    val replyMarkup: InlineKeyboardMarkup? = null
): JsonTgCallable<Boolean>()
