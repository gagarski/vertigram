package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class SetGameScore(
    val userId: Long,
    val score: Int,
    val force: Boolean = false,
    val disableEditMessage: Boolean = false,
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: Long? = null
) : JsonTgCallable<Boolean>
