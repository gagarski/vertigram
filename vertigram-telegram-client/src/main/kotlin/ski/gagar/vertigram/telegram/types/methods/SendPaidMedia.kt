package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.*
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedCaption
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to send paid media.
 *
 * See Telegram's [sendPaidMedia](https://core.telegram.org/bots/api#sendpaidmedia) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendPaidMedia internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be sent. */
    val directMessagesTopicId: Long? = null,
    /** Number of Telegram Stars the user must pay to access the media. */
    val starCount: Int,
    /** Media to send. */
    val media: List<InputMedia.Paid>,
    /** Bot-defined payload. */
    val payload: String? = null,
    /** Caption of the media. */
    override val caption: String? = null,
    /** Mode for parsing entities in the caption or text. */
    override val parseMode: FormattedText.ParseMode? = null,
    /** Special entities that appear in the caption; can be specified instead of [parseMode]. */
    override val captionEntities: List<MessageEntity>? = null,
    /** Pass `true` to show the caption above the message media. */
    val showCaptionAboveMedia: Boolean = false,
    /** Sends the message silently. */
    val disableNotification: Boolean = false,
    /** Protects the sent message from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Pass `true` to allow up to 1000 messages per second for a fee in Telegram Stars. */
    val allowPaidBroadcast: Boolean = false,
    /** Parameters of the suggested post to send. */
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    /** Parameters of the message being replied to. */
    val replyParameters: ReplyParameters? = null,
    /** Additional interface options. */
    val replyMarkup: ReplyMarkup? = null
) : MultipartTelegramCallable<Message>(), HasChatId, HasOptionalFormattedCaption
