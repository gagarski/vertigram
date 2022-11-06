package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vxutil.vertigram.types.Poll

@TgMethod
@Throttled
data class StopPoll(
    override val chatId: ChatId,
    val messageId: Long,
    val replyMarkup: InlineKeyboardMarkup? = null
): JsonTgCallable<Poll>(), HasChatId
