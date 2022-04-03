package ski.gagar.vxutil.vertigram.methods

data class DeleteMessage(val chatId: Long, val messageId: Long): JsonTgCallable<Boolean>()
