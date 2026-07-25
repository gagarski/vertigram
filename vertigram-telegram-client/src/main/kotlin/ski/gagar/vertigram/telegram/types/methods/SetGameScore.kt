package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message

/**
 * Use this method to set the score of the specified user in a game message.
 *
 * See Telegram's [setGameScore](https://core.telegram.org/bots/api#setgamescore) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(SetGameScore.InlineMessage::class),
    JsonSubTypes.Type(SetGameScore.ChatMessage::class)
)
sealed interface SetGameScore {
    val userId: Long
    val score: Int
    val force: Boolean
    val disableEditMessage: Boolean
    /**
     * Case when the game message is an inline message. Returns `true` on success.
     */
    @TelegramCodegen.Method(
        name = "setGameScore"
    )
    @Throttled
    data class InlineMessage internal constructor(
        /** User identifier. */
        override val userId: Long,
        /** New score; must be non-negative. */
        override val score: Int,
        /** Pass `true` to allow the score to decrease. */
        override val force: Boolean = false,
        /** Pass `true` to prevent the game message from being automatically edited. */
        override val disableEditMessage: Boolean = false,
        /** Identifier of the inline message. */
        val inlineMessageId: String
    ) : SetGameScore, JsonTelegramCallable<Boolean>()

    /**
     * Case when the game message belongs to a chat. Returns the edited message on success.
     */
    @TelegramCodegen.Method(
        name = "setGameScore"
    )
    @Throttled
    data class ChatMessage internal constructor(
        /** User identifier. */
        override val userId: Long,
        /** New score; must be non-negative. */
        override val score: Int,
        /** Pass `true` to allow the score to decrease. */
        override val force: Boolean = false,
        /** Pass `true` to prevent the game message from being automatically edited. */
        override val disableEditMessage: Boolean = false,
        /** Unique identifier of the target chat. */
        override val chatId: Long,
        /** Identifier of the sent message. */
        val messageId: Long
    ) : SetGameScore, HasChatIdLong, JsonTelegramCallable<Message>()
}
