package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [pinChatMessage](https://core.telegram.org/bots/api#pinchatmessage) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class PinChatMessage(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
