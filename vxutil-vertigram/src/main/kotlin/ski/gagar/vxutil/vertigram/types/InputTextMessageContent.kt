package ski.gagar.vxutil.vertigram.types

data class InputTextMessageContent(
    val messageText: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    // Since Telegram Bot API 7.0
    val linkPreviewOptions: LinkPreviewOptions? = null
) : InputMessageContent
