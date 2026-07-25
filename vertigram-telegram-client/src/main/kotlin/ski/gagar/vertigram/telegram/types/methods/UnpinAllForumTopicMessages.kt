package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to clear the list of pinned messages in a forum topic. Returns `true` on success.
 *
 * See Telegram's
 * [unpinAllForumTopicMessages](https://core.telegram.org/bots/api#unpinallforumtopicmessages) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class UnpinAllForumTopicMessages internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread of the forum topic. */
    val messageThreadId: Long,
) : JsonTelegramCallable<Boolean>(), HasChatId
