package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [declineChatJoinRequest](https://core.telegram.org/bots/api#declinechatjoinrequest) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class DeclineChatJoinRequest(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val userId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
