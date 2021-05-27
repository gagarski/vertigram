package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class LeaveChat(
    val chatId: Long
) : JsonTgCallable<Boolean>()
