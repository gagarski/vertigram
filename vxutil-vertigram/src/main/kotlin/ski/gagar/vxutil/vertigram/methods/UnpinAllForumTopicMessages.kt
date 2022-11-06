package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
@Throttled
data class UnpinAllForumTopicMessages(
    override val chatId: ChatId,
    val messageThreadId: Long,
) : JsonTgCallable<Boolean>(), HasChatId
