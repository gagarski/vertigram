package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [editForumTopic](https://core.telegram.org/bots/api#editforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class EditForumTopic internal constructor(
    override val chatId: ChatId,
    val messageThreadId: Long,
    val name: String? = null,
    val iconCustomEmojiId: String? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
