package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId

data class SetGameScore(
    val userId: Long,
    val score: Long,
    val force: Boolean = false,
    val disableEditMessage: Boolean = false,
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: Long? = null
) : JsonTgCallable<Boolean>()
