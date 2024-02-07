package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [banChatSenderChat](https://core.telegram.org/bots/api#banchatsenderchat) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgMethod
data class BanChatSenderChat(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val senderChatId: Long
) : JsonTgCallable<Boolean>(), HasChatId
