package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [banChatSenderChat](https://core.telegram.org/bots/api#banchatsenderchat) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class BanChatSenderChat(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val senderChatId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
