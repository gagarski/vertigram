package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [reopenGeneralForumTopic](https://core.telegram.org/bots/api#reopengeneralforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class ReopenGeneralForumTopic(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
