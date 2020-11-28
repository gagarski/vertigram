package ski.gagar.vertigram.entities.requests

data class SetChatAdministatorCustomTitle(
    val chatId: Long,
    val userId: Long,
    val customTitle: String
) : JsonTgCallable<Boolean>()
