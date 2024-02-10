package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [deleteForumTopic](https://core.telegram.org/bots/api#deleteforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class DeleteForumTopic(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long,
) : JsonTelegramCallable<Boolean>(), HasChatId
