package ski.gagar.vertigram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.types.util.ChatId

@TelegramCodegen(
    generateMethod = false,
    generatePseudoConstructor = true,
    generateRichTextWrappers = true
)
data class ReplyParameters internal constructor(
    val messageId: Long,
    val chatId: ChatId? = null,
    val allowSendingWithoutReply: Boolean = false,
    val quote: String? = null,
    val quoteParseMode: ParseMode? = null,
    val quoteEntities: List<MessageEntity>? = null,
    val quotePosition: Int? = null
) {
    companion object
}
