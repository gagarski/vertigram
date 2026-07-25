package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Poll
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to stop a poll which was sent by the bot. Returns the stopped [Poll] on success.
 *
 * See Telegram's [stopPoll](https://core.telegram.org/bots/api#stoppoll) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class StopPoll internal constructor(
    /** Unique identifier of the business connection on behalf of which the message was sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Identifier of the original message with the poll. */
    val messageId: Long,
    /** New inline keyboard for the message. */
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null
): JsonTelegramCallable<Poll>(), HasChatId
