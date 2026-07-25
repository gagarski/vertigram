package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to send a group of photos, videos, documents or audio as an album.
 *
 * See Telegram's [sendMediaGroup](https://core.telegram.org/bots/api#sendmediagroup) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendMediaGroup internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the messages will be sent. */
    val directMessagesTopicId: Long? = null,
    /** Two to ten media items describing the album. */
    val media: List<InputMedia>,
    /** Sends the messages silently. */
    val disableNotification: Boolean = false,
    /** Protects the messages from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Pass `true` to allow up to 1000 messages per second for a fee in Telegram Stars. */
    val allowPaidBroadcast: Boolean = false,
    /** Unique identifier of the message effect added to the messages. */
    val messageEffectId: String? = null,
    /** Parameters of the message being replied to. */
    val replyParameters: ReplyParameters? = null
) : MultipartTelegramCallable<List<Message>>(), HasChatId
