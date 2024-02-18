package ski.gagar.vertigram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.types.richtext.HasOptionalRichQuote
import ski.gagar.vertigram.types.util.ChatId

@TelegramCodegen(
    generateMethod = false,
    generatePseudoConstructor = true,
    wrapRichText = true
)
data class ReplyParameters internal constructor(
    val messageId: Long,
    val chatId: ChatId? = null,
    val allowSendingWithoutReply: Boolean = false,
    override val quote: String? = null,
    override val quoteParseMode: ParseMode? = null,
    override val quoteEntities: List<MessageEntity>? = null,
    val quotePosition: Int? = null
) : HasOptionalRichQuote {
    companion object
}
