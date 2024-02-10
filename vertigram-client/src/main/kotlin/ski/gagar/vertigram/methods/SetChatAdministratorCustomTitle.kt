package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId

data class SetChatAdministratorCustomTitle(
    override val chatId: ChatId,
    val userId: Long,
    val customTitle: String
) : JsonTelegramCallable<Boolean>(), HasChatId
