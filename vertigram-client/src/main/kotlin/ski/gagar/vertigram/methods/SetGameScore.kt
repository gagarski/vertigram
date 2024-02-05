package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId

@TgMethod
@Throttled
data class SetGameScore(
    val userId: Long,
    val score: Int,
    val force: Boolean = false,
    val disableEditMessage: Boolean = false,
    override val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: Long? = null
) : JsonTgCallable<Boolean>(), HasChatId
