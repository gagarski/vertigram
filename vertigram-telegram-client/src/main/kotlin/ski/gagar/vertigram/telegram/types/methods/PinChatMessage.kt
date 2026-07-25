package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to add a message to the list of pinned messages in a chat.
 *
 * Returns `true` on success.
 *
 * See Telegram's [pinChatMessage](https://core.telegram.org/bots/api#pinchatmessage) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class PinChatMessage internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be pinned. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Identifier of the message to pin. */
    val messageId: Long,
    /** Pass `true` if it is not necessary to send a notification to all chat members about the new pinned message. */
    val disableNotification: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
