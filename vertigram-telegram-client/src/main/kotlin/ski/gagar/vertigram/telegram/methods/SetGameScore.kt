package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message

/**
 * Telegram [setGameScore](https://core.telegram.org/bots/api#setgamescore) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
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
     * Inline message case
     */
    @TelegramMethod(
        methodName = "setGameScore"
    )
    @TelegramCodegen(
        methodName = "setGameScore",
        generatePseudoConstructor = true,
        pseudoConstructorName = "SetGameScore"
    )
    @Throttled
    data class InlineMessage internal constructor(
        override val userId: Long,
        override val score: Int,
        override val force: Boolean = false,
        override val disableEditMessage: Boolean = false,
        val inlineMessageId: String
    ) : SetGameScore, JsonTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "setGameScore"
    )
    @TelegramCodegen(
        methodName = "setGameScore",
        generatePseudoConstructor = true,
        pseudoConstructorName = "SetGameScore"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val userId: Long,
        override val score: Int,
        override val force: Boolean = false,
        override val disableEditMessage: Boolean = false,
        override val chatId: Long,
        val messageId: Long
    ) : SetGameScore, HasChatIdLong, JsonTelegramCallable<Message>()
}