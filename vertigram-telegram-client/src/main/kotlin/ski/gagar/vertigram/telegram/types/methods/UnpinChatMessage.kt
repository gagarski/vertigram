package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to remove a message from the list of pinned messages in a chat. Returns `true` on success.
 *
 * See Telegram's [unpinChatMessage](https://core.telegram.org/bots/api#unpinchatmessage) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class UnpinChatMessage internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be unpinned. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Identifier of the message to unpin. */
    val messageId: Long? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
