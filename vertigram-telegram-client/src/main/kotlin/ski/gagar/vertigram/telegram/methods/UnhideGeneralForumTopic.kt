package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [unhideGeneralForumTopic](https://core.telegram.org/bots/api#unhidegeneralforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class UnhideGeneralForumTopic(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
