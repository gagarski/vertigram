package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class CloseForumTopic(
    override val chatId: ChatId,
    val messageThreadId: Long,
) : JsonTgCallable<Boolean>(), HasChatId
