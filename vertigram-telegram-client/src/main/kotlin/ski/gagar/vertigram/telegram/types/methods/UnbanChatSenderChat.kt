package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [unbanChatSenderChat](https://core.telegram.org/bots/api#unbanchatsenderchat) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class UnbanChatSenderChat internal constructor(
    override val chatId: ChatId,
    val senderChatId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
