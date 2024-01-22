package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.*

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
