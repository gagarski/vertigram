package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.*

@TgMethod
@Throttled
data class SendMessage(
    override val chatId: ChatId,
    val text: String,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyMarkup: ReplyMarkup? = null,
    // Since Telegram Bot Api 6.4
    val messageThreadId: Long? = null,
    // Since Telegram Bot API 7.0
    val replyParameters: ReplyParameters? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null
) : JsonTgCallable<Message>(), HasChatId
