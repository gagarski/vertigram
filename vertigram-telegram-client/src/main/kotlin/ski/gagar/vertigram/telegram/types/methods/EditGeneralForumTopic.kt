package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [editGeneralForumTopic](https://core.telegram.org/bots/api#editgeneralforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class EditGeneralForumTopic internal constructor(
    override val chatId: ChatId,
    val name: String
) : JsonTelegramCallable<Boolean>(), HasChatId
