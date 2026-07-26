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
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedCaption
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to send general files.
 *
 * See Telegram's [sendDocument](https://core.telegram.org/bots/api#senddocument) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendDocument internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be sent. */
    val directMessagesTopicId: Long? = null,
    /** File to send. */
    val document: Attachment,
    /** Thumbnail of the file sent. */
    val thumbnail: Attachment? = null,
    /** Caption of the media. */
    override val caption: String? = null,
    /** Mode for parsing entities in the caption or text. */
    override val parseMode: FormattedText.ParseMode? = null,
    /** Special entities that appear in the caption; can be specified instead of [parseMode]. */
    override val captionEntities: List<MessageEntity>? = null,
    /** Disables automatic server-side content type detection for the uploaded file. */
    val disableContentTypeDetection: Boolean = false,
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
) : MultipartTelegramCallable<Message>(), HasChatId, HasReceiverUserId, HasOptionalFormattedCaption
