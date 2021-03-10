package ski.gagar.vertigram.entities.requests

data class SetChatAdministratorCustomTitle(
    val chatId: Long,
    val userId: Long,
    val customTitle: String
) : JsonTgCallable<Boolean>()
