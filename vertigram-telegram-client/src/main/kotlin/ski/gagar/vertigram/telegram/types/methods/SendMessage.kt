package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.richtext.HasRichText
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to send text messages.
 *
 * See Telegram's [sendMessage](https://core.telegram.org/bots/api#sendmessage) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendMessage internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be sent. */
    val directMessagesTopicId: Long? = null,
    /** Text of the message. */
    override val text: String,
    /** Mode for parsing entities in the caption or text. */
    override val parseMode: RichText.ParseMode? = null,
    /** Special entities that appear in [text]; can be specified instead of [parseMode]. */
    override val entities: List<MessageEntity>? = null,
    /** Link preview generation options for the message. */
    val linkPreviewOptions: Message.LinkPreviewOptions? = null,
    /** Sends the message silently. */
    val disableNotification: Boolean = false,
    /** Protects the sent message from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Pass `true` to allow up to 1000 messages per second for a fee in Telegram Stars. */
    val allowPaidBroadcast: Boolean = false,
    /** Unique identifier of the message effect added to the message. */
    val messageEffectId: String? = null,
    /** Parameters of the suggested post to send. */
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    /** Unique identifier of the recipient user for an ephemeral message. */
    override val receiverUserId: Long? = null,
    /** Unique identifier of the callback query that allows sending an ephemeral message. */
    val callbackQueryId: String? = null,
    /** Parameters of the message being replied to. */
    val replyParameters: ReplyParameters? = null,
    /** Additional interface options. */
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId, HasReceiverUserId, HasRichText
