package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.LinkPreviewOptions
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode
import ski.gagar.vertigram.types.ReplyMarkup

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
) : JsonTelegramCallable<Message>(), HasChatId
