package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
data class DeleteMessage(val chatId: Long, val messageId: Long): JsonTgCallable<Boolean>()
