package ski.gagar.vxutil.vertigram.methods

data class LeaveChat(
    val chatId: Long
) : JsonTgCallable<Boolean>()
