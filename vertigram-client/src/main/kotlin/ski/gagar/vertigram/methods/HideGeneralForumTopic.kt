package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId

@Throttled
data class HideGeneralForumTopic(
    override val chatId: ChatId
) : JsonTelegramCallable<Boolean>(), HasChatId
