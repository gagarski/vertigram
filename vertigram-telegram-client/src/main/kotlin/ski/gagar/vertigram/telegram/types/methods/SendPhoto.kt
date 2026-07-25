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
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to send photos. On success, the sent [Message] is returned.
 */
@Throttled
@TelegramCodegen.Method
data class SendPhoto internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be sent. */
    val directMessagesTopicId: Long? = null,
    /**
     * Photo to send.
     *
     * [Attachment] accepts a Telegram file identifier, an HTTP URL, or a new file upload. The photo must be at most
     * 10 MB. Its width and height must not exceed 10000 in total, and their ratio must be at most 20.
     */
    val photo: Attachment,
    /** Photo caption, 0-1024 characters after entities parsing. */
    override val caption: String? = null,
    /** Mode for parsing entities in the photo caption. */
    override val parseMode: RichText.ParseMode? = null,
    /** Special entities that appear in the caption, which can be specified instead of the parsing mode. */
    override val captionEntities: List<MessageEntity>? = null,
    /** Pass `true` if the caption must be shown above the message media. */
    val showCaptionAboveMedia: Boolean = false,
    /** Pass `true` if the photo needs to be covered with a spoiler animation. */
    val hasSpoiler: Boolean = false,
    /** Sends the message silently. Users will receive a notification with no sound. */
    val disableNotification: Boolean = false,
    /** Protects the contents of the sent message from forwarding and saving. */
    val protectContent: Boolean = false,
    /**
     * Pass `true` to allow up to 1000 messages per second for a fee of 0.1 Telegram Stars per message.
     */
    val allowPaidBroadcast: Boolean = false,
    /** Unique identifier of the message effect to be added to the message; for private chats only. */
    val messageEffectId: String? = null,
    /** Parameters of the suggested post to send; for direct messages chats only. */
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    /** For outgoing ephemeral messages, unique identifier of the user who will receive the message. */
    override val receiverUserId: Long? = null,
    /** For outgoing ephemeral messages, identifier of the callback query which triggered the message. */
    val callbackQueryId: String? = null,
    /** Description of the message to reply to. */
    val replyParameters: ReplyParameters? = null,
    /** Additional interface options. */
    val replyMarkup: ReplyMarkup? = null
) : MultipartTelegramCallable<Message>(), HasChatId, HasReceiverUserId, HasOptionalRichCaption
