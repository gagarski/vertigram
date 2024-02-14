package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatIdLong
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message

/**
 * Telegram [setGameScore](https://core.telegram.org/bots/api#setgamescore) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface SetGameScore {
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
        val userId: Long,
        val score: Int,
        val force: Boolean = false,
        val disableEditMessage: Boolean = false,
        val inlineMessageId: Long
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
        val userId: Long,
        val score: Int,
        val force: Boolean = false,
        val disableEditMessage: Boolean = false,
        override val chatId: Long,
        val messageId: Long
    ) : SetGameScore, HasChatIdLong, JsonTelegramCallable<Message>()
}