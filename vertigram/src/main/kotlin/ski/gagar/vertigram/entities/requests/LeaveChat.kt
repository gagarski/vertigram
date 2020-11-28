package ski.gagar.vertigram.entities.requests

data class LeaveChat(
    val chatId: Long
) : JsonTgCallable<Boolean>()