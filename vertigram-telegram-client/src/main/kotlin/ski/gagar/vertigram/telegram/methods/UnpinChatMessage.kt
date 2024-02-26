package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [unpinChatMessage](https://core.telegram.org/bots/api#unpinchatmessage) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class UnpinChatMessage(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageId: Long? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
