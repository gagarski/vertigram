package ski.gagar.vxutil.vertigram.methods

data class SetChatAdministratorCustomTitle(
    val chatId: Long,
    val userId: Long,
    val customTitle: String
) : JsonTgCallable<Boolean>()
