package ski.gagar.vxutil.vertigram.types

data class InputTextMessageContent(
    val messageText: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val disableWebPagePreview: Boolean = false
) : InputMessageContent
