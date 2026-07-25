package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.GameHighScore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get data for high score tables.
 *
 * Will return the score of the specified user and several of their neighbors in a game.
 *
 * See Telegram's [getGameHighScores](https://core.telegram.org/bots/api#getgamehighscores) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(GetGameHighScores.InlineMessage::class),
    JsonSubTypes.Type(GetGameHighScores.ChatMessage::class)
)
@TelegramCodegen.Method
sealed class GetGameHighScores : JsonTelegramCallable<List<GameHighScore>>() {
    abstract val userId: Long
    /**
     * Case when the game message is an inline message.
     */
    @TelegramCodegen.Method(
        name = "getGameHighScores"
    )
    data class InlineMessage internal constructor(
        /** Target user identifier. */
        override val userId: Long,
        /** Identifier of the inline message. */
        val inlineMessageId: String
    ) : GetGameHighScores()

    /**
     * Case when the game message belongs to a chat.
     */
    @TelegramCodegen.Method(
        name = "getGameHighScores"
    )
    data class ChatMessage internal constructor(
        /** Target user identifier. */
        override val userId: Long,
        /** Unique identifier of the target chat. */
        override val chatId: Long,
        /** Identifier of the sent message. */
        val messageId: Long,
    ) : GetGameHighScores(), HasChatIdLong
}
