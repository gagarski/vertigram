package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vertigram.types.Poll

@TgMethod
@Throttled
data class StopPoll(
    override val chatId: ChatId,
    val messageId: Long,
    val replyMarkup: InlineKeyboardMarkup? = null
): JsonTgCallable<Poll>(), HasChatId
