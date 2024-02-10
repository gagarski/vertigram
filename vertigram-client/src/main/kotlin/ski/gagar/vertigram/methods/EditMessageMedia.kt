package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vertigram.types.InputMedia
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.annotations.TelegramMedia

@Throttled
data class EditMessageMedia(
    @TelegramMedia
    val media: InputMedia,
    override val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: Long? = null,
    val replyMarkup: InlineKeyboardMarkup? = null
) : MultipartTelegramCallable<Message>(), HasChatId
