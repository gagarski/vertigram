package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatIdLong
import ski.gagar.vertigram.types.GameHighScore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getGameHighScores](https://core.telegram.org/bots/api#getgamehighscopes) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class GetGameHighScores : JsonTelegramCallable<List<GameHighScore>>() {
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "getGameHighScores"
    )
    @TelegramCodegen(
        methodName = "getGameHighScores",
        generatePseudoConstructor = true,
        pseudoConstructorName = "GetGameHighScores"
    )
    data class InlineMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val userId: Long,
        val inlineMessageId: String
    ) : GetGameHighScores()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "getGameHighScores"
    )
    @TelegramCodegen(
        methodName = "getGameHighScores",
        generatePseudoConstructor = true,
        pseudoConstructorName = "GetGameHighScores"
    )
    data class ChatMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val userId: Long,
        override val chatId: Long,
        val messageId: Long,
    ) : GetGameHighScores(), HasChatIdLong
}