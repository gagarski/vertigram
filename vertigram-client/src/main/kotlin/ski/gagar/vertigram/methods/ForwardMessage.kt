package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [forwardMessage](https://core.telegram.org/bots/api#forwardmessage) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class ForwardMessage(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val fromChatId: ChatId,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val messageId: Long
) : JsonTelegramCallable<Message>(), HasChatId
