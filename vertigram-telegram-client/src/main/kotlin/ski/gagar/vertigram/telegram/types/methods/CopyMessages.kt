package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Poll
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to copy messages of any kind. If some of the specified messages can't be found or copied, they are
 * skipped. Service messages, paid media messages, giveaway messages, giveaway winners messages, and invoice messages
 * can't be copied. A quiz [Poll] can be copied only if the value of [Poll.Quiz.correctOptionIds] is known to the bot.
 * The method is analogous to [ski.gagar.vertigram.telegram.methods.forwardMessages], but the copied messages don't
 * have a link to the original message.
 *
 * Album grouping is kept for copied messages. On success, a list of [Message.Id] values of the sent messages is
 * returned.
 *
 * See Telegram's [copyMessages](https://core.telegram.org/bots/api#copymessages) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class CopyMessages internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /**
     * Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private chats of
     * bots with forum topic mode enabled only.
     */
    val messageThreadId: Long? = null,
    /**
     * Identifier of the direct messages topic to which the messages will be sent; required if the messages are sent to
     * a direct messages chat.
     */
    val directMessagesTopicId: Long? = null,
    /** Unique identifier for the source chat or username of the source bot, supergroup, or channel. */
    val fromChatId: ChatId,
    /**
     * List of 1-100 identifiers of messages in [fromChatId] to copy. The identifiers must be specified in a strictly
     * increasing order.
     */
    val messageIds: List<Long>,
    /** Sends the messages silently. Users will receive a notification with no sound. */
    val disableNotification: Boolean = false,
    /** Protects the contents of the sent messages from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Pass `true` to copy the messages without their captions. */
    val removeCaption: Boolean = false
) : JsonTelegramCallable<List<Message.Id>>(), HasChatId
