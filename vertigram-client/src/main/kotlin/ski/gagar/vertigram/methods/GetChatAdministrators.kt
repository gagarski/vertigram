package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.ChatMember
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getChatAdministrators](https://core.telegram.org/bots/api#getchatadministrators) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetChatAdministrators(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId
) : JsonTelegramCallable<List<ChatMember>>(), HasChatId
