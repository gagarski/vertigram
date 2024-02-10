package ski.gagar.vertigram.methods

import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.GameHighScore

data class GetGameHighScores(
    val userId: Long,
    override val chatId: ChatId? = null,
    val messageId: Long? = null,
    val inlineMessageId: String? = null
) : JsonTelegramCallable<List<GameHighScore>>(), HasChatId
