package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.GameHighScore

@TgMethod
data class GetGameHighScores(
    val userId: Long,
    val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null
) : JsonTgCallable<List<GameHighScore>>
