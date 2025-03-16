package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [forwardMessages](https://core.telegram.org/bots/api#forwardmessages) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class ForwardMessages internal constructor(
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false
) : JsonTelegramCallable<List<Message.Id>>(), HasChatId
