package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class SetChatAdministratorCustomTitle(
    val chatId: Long,
    val userId: Long,
    val customTitle: String
) : JsonTgCallable<Boolean>()
