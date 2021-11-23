package ski.gagar.vxutil.vertigram.entities.requests

data class LeaveChat(
    val chatId: Long
) : JsonTgCallable<Boolean>()
