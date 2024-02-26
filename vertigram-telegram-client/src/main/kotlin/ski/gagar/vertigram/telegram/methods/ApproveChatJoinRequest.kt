package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [approveChatJoinRequest](https://core.telegram.org/bots/api#approvechatjoinrequest) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ApproveChatJoinRequest(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val userId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
