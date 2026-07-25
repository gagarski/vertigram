package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to send phone contacts.
 *
 * See Telegram's [sendContact](https://core.telegram.org/bots/api#sendcontact) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendContact internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be sent. */
    val directMessagesTopicId: Long? = null,
    /** Contact's phone number. */
    val phoneNumber: String,
    /** Contact's first name. */
    val firstName: String,
    /** Contact's last name. */
    val lastName: String? = null,
    /** Additional contact data in vCard format. */
    val vcard: String? = null,
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
) : JsonTelegramCallable<Message>(), HasChatId, HasReceiverUserId
