package ski.gagar.vertigram.types

data class ReplyParameters(
    val messageId: Long,
    val chatId: ChatId? = null,
    val allowSendingWithoutReply: Boolean = false,
    val quote: String? = null,
    val quoteParseMode: ParseMode? = null,
    val quoteEntities: List<MessageEntity>? = null,
    val quotePosition: Int? = null
)
