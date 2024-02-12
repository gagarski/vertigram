package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vertigram.types.Poll
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [stopPoll](https://core.telegram.org/bots/api#stoppoll) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class StopPoll(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageId: Long,
    val replyMarkup: InlineKeyboardMarkup? = null
): JsonTelegramCallable<Poll>(), HasChatId
