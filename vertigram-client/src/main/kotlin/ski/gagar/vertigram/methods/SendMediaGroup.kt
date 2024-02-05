package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.InputMedia
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.util.multipart.TgMedia

@TgMethod
@Throttled
data class SendMediaGroup(
    override val chatId: ChatId,
    @TgMedia
    val media: List<InputMedia>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    // Since Telegram Bot Api 6.3
    val messageThreadId: Long? = null,
    // Since Telegram Bot API 7.0
    val replyParameters: ReplyParameters? = null
) : MultipartTgCallable<List<Message>>(), HasChatId
