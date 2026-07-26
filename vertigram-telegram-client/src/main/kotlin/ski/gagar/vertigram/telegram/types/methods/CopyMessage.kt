package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.Poll
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedCaption
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration

/**
 * Use this method to copy messages of any kind. Service messages, paid media messages, giveaway messages, giveaway
 * winners messages, and invoice messages can't be copied. A quiz [Poll] can be copied only if the value of
 * [Poll.Quiz.correctOptionIds] is known to the bot. The method is analogous to
 * [ski.gagar.vertigram.telegram.methods.forwardMessage], but the copied message doesn't have a link to the original
 * message. Returns the [Message.Id] of the sent message on success.
 *
 * See Telegram's [copyMessage](https://core.telegram.org/bots/api#copymessage) documentation.
 */
@TelegramCodegen.Method
@Throttled
data class CopyMessage internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of
     * bots with forum topic mode enabled only.
     */
    val messageThreadId: Long? = null,
    /**
     * Identifier of the direct messages topic to which the message will be sent; required if the message is sent to a
     * direct messages chat.
     */
    val directMessagesTopicId: Long? = null,
    /** Unique identifier for the source chat or username of the source bot, supergroup, or channel. */
    val fromChatId: ChatId,
    /** Message identifier in the chat specified in [fromChatId]. */
    val messageId: Long,
    /** New start timestamp for the copied video in the message. */
    val videoStartTimestamp: Duration? = null,
    /** New caption for media, 0-1024 characters after entities parsing. If omitted, the original caption is kept. */
    override val caption: String? = null,
    /**
     * Mode for parsing entities in the new caption. See Telegram's
     * [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
     */
    override val parseMode: FormattedText.ParseMode? = null,
    /** List of special entities that appear in the new caption, which can be specified instead of [parseMode]. */
    override val captionEntities: List<MessageEntity>? = null,
    /** Pass `true` if the caption must be shown above the message media. Ignored if a new caption isn't specified. */
    val showCaptionAboveMedia: Boolean = false,
    /** Sends the message silently. Users will receive a notification with no sound. */
    val disableNotification: Boolean = false,
    /** Protects the contents of the sent message from forwarding and saving. */
    val protectContent: Boolean = false,
    /**
     * Pass `true` to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1 Telegram
     * Stars per message. The relevant Stars will be withdrawn from the bot's balance.
     */
    val allowPaidBroadcast: Boolean = false,
    /**
     * Unique identifier of the message effect to be added to the message; only available when copying to private
     * chats.
     */
    val messageEffectId: String? = null,
    /**
     * Parameters of the suggested post to send; for direct messages chats only. If the message is sent as a reply to
     * another suggested post, then that suggested post is automatically declined.
     */
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    /** Description of the message to reply to. */
    val replyParameters: ReplyParameters? = null,
    /**
     * Additional interface options for an inline keyboard, custom reply keyboard, instructions to remove a reply
     * keyboard or to force a reply from the user.
     */
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message.Id>(), HasChatId, HasOptionalFormattedCaption

