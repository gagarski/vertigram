package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.*

@TgMethod
@Throttled
data class EditMessageText(
    val text: String,
    override val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageid: Long? = null,
    val parseMode: ParseMode? = null,
    val entities: List<MessageEntity>? = null,
    val replyMarkup: ReplyMarkup? = null,
    // Since Telegram Bot API 7.0
    val linkPreviewOptions: LinkPreviewOptions? = null
) : JsonTgCallable<Message>(), HasChatId
