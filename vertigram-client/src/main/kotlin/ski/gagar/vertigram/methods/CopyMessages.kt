package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.MessageId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [copyMessages](https://core.telegram.org/bots/api#copymessages) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class CopyMessages(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val fromChatId: ChatId,
    val messageIds: List<Long>,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val removeCaption: Boolean = false
) : JsonTelegramCallable<List<MessageId>>(), HasChatId
