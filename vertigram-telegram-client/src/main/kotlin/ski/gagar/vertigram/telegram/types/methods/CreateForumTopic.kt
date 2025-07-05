package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ForumTopic

/**
 * Telegram [createForumTopic](https://core.telegram.org/bots/api#createforumtopic) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class CreateForumTopic internal constructor(
    override val chatId: ChatId,
    val name: String,
    val iconColor: ForumTopic.Color? = null,
    val iconCustomEmojiId: String? = null
) : JsonTelegramCallable<ForumTopic>(), HasChatId
