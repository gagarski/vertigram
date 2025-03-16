package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [reopenForumTopic](https://core.telegram.org/bots/api#reopenforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ReopenForumTopic internal constructor(
    override val chatId: ChatId,
    val messageThreadId: Long,
) : JsonTelegramCallable<Boolean>(), HasChatId
