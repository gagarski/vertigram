package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.InputMedia
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [sendMediaGroup](https://core.telegram.org/bots/api#sendmediagroup) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SendMediaGroup(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    @TelegramMedia
    val media: List<InputMedia>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyParameters: ReplyParameters? = null
) : MultipartTelegramCallable<List<Message>>(), HasChatId
