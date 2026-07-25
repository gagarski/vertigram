package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichQuote
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Describes reply parameters for a message.
 *
 * Exactly one of [messageId] and [ephemeralMessageId] must be specified.
 *
 * See Telegram's [ReplyParameters](https://core.telegram.org/bots/api#replyparameters) documentation.
 */
@TelegramCodegen.Type
data class ReplyParameters internal constructor(
    /** Identifier of the message that will be replied to. */
    val messageId: Long? = null,
    /** Identifier of the ephemeral message that will be replied to. */
    val ephemeralMessageId: Long? = null,
    /** Unique identifier for the chat containing the message being replied to. */
    val chatId: ChatId? = null,
    /** Whether the message should be sent even if the specified replied-to message is not found. */
    val allowSendingWithoutReply: Boolean = false,
    /** Quoted part of the message being replied to. */
    override val quote: String? = null,
    /** Mode for parsing entities in [quote]. */
    override val quoteParseMode: RichText.ParseMode? = null,
    /** Special entities that appear in [quote], specified instead of [quoteParseMode]. */
    override val quoteEntities: List<MessageEntity>? = null,
    /** Position of [quote] in the original message in UTF-16 code units. */
    val quotePosition: Int? = null,
    /** Identifier of the checklist task that will be replied to. */
    val checklistTaskId: Int? = null,
    /** Identifier of the poll option that will be replied to. */
    val pollOptionId: String? = null
) : HasOptionalRichQuote {
    companion object
}
