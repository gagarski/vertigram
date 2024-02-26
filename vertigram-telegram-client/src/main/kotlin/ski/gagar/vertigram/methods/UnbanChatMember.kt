package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [unbanChatMember](https://core.telegram.org/bots/api#unbanchatmember) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class UnbanChatMember(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val userId: Long,
    val onlyIfBanned: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
