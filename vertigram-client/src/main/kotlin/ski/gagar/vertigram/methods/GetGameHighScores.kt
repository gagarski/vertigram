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
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class GetGameHighScores : JsonTelegramCallable<List<GameHighScore>>() {
    abstract val userId: Long
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
        override val userId: Long,
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
        override val userId: Long,
        override val chatId: Long,
        val messageId: Long,
    ) : GetGameHighScores(), HasChatIdLong
}