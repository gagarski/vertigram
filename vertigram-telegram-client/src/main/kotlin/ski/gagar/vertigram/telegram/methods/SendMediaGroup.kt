package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [sendMediaGroup](https://core.telegram.org/bots/api#sendmediagroup) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen
data class SendMediaGroup(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    @TelegramMedia
    val media: List<InputMedia>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyParameters: ReplyParameters? = null
) : MultipartTelegramCallable<List<Message>>(), HasChatId
