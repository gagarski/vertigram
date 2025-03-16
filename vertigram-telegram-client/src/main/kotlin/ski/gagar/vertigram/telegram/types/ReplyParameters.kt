package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichQuote
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ReplyParameters](https://core.telegram.org/bots/api#replyparameters) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class ReplyParameters internal constructor(
    val messageId: Long,
    val chatId: ChatId? = null,
    val allowSendingWithoutReply: Boolean = false,
    override val quote: String? = null,
    override val quoteParseMode: RichText.ParseMode? = null,
    override val quoteEntities: List<MessageEntity>? = null,
    val quotePosition: Int? = null
) : HasOptionalRichQuote {
    companion object
}
