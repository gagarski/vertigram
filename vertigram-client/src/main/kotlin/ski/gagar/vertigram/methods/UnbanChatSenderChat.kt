package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [unbanChatSenderChat](https://core.telegram.org/bots/api#unbanchatsenderchat) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class UnbanChatSenderChat(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val senderChatId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
