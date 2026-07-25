package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to forward multiple messages of any kind.
 *
 * If some of the specified messages can't be found or forwarded, they are skipped. Service messages and messages
 * with protected content can't be forwarded. Album grouping is kept for forwarded messages.
 *
 * See Telegram's [forwardMessages](https://core.telegram.org/bots/api#forwardmessages) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class ForwardMessages internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the messages will be forwarded. */
    val directMessagesTopicId: Long? = null,
    /** Unique identifier for the source chat or username of the source bot, supergroup, or channel. */
    val fromChatId: ChatId,
    /** List of 1-100 message identifiers in [fromChatId], specified in strictly increasing order. */
    val messageIds: List<Long>,
    /** Sends the messages silently. Users will receive a notification with no sound. */
    val disableNotification: Boolean = false,
    /** Protects the contents of the forwarded messages from forwarding and saving. */
    val protectContent: Boolean = false
) : JsonTelegramCallable<List<Message.Id>>(), HasChatId
