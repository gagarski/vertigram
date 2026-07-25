package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Use this method to forward messages of any kind.
 *
 * Service messages and messages with protected content can't be forwarded. Returns the sent [Message] on success.
 *
 * See Telegram's [forwardMessage](https://core.telegram.org/bots/api#forwardmessage) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class ForwardMessage internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be forwarded. */
    val directMessagesTopicId: Long? = null,
    /** Unique identifier for the source chat or username of the source bot, supergroup, or channel. */
    val fromChatId: ChatId,
    /** New start timestamp for the forwarded video in the message. */
    val videoStartTimestamp: Duration? = null,
    /** Sends the message silently. Users will receive a notification with no sound. */
    val disableNotification: Boolean = false,
    /** Protects the contents of the forwarded message from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Unique identifier of the message effect to be added to the message; only available in private chats. */
    val messageEffectId: String? = null,
    /** Parameters of the suggested post to send; for direct messages chats only. */
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    /** Message identifier in the chat specified in [fromChatId]. */
    val messageId: Long
) : JsonTelegramCallable<Message>(), HasChatId
