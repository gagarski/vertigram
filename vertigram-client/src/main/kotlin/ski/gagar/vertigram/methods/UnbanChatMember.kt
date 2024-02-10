package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

data class UnbanChatMember(
    override val chatId: ChatId,
    val userId: Long,
    val onlyIfBanned: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
