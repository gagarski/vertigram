package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setChatTitle](https://core.telegram.org/bots/api#setchattitle) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SetChatTitle(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val title: String
) : JsonTelegramCallable<Boolean>(), HasChatId
