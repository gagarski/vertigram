package ski.gagar.vxutil.vertigram.entities.requests

data class DeleteMessage(val chatId: Long, val messageId: Long): JsonTgCallable<Boolean>()
