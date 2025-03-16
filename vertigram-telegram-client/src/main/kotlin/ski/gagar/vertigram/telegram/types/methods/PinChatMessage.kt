package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [pinChatMessage](https://core.telegram.org/bots/api#pinchatmessage) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class PinChatMessage internal constructor(
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val messageId: Long,
    val disableNotification: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
