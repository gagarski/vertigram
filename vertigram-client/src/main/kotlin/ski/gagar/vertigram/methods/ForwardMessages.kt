package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [forwardMessages](https://core.telegram.org/bots/api#forwardmessages) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class ForwardMessages(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false
) : JsonTelegramCallable<List<Message.Id>>(), HasChatId
