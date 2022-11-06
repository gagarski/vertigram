package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class SetChatAdministratorCustomTitle(
    override val chatId: ChatId,
    val userId: Long,
    val customTitle: String
) : JsonTgCallable<Boolean>(), HasChatId
