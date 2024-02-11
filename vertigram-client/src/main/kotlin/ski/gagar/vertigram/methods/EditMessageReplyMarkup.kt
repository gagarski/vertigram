package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.InlineKeyboardMarkup
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageReplyMarkup](https://core.telegram.org/bots/api#editmessagereplymarkup) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface EditMessageReplyMarkup {
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageReplyMarkup"
    )
    @TelegramCodegen(
        methodName = "editMessageReplyMarkup",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageReplyMarkup"
    )
    @Throttled
    data class InlineMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inlineMessageId: Long,
        val replyMarkup: InlineKeyboardMarkup? = null
    ) : EditMessageReplyMarkup, JsonTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageReplyMarkup"
    )
    @TelegramCodegen(
        methodName = "editMessageReplyMarkup",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageReplyMarkup"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val chatId: ChatId,
        val messageId: Long,
        val replyMarkup: InlineKeyboardMarkup? = null
    ) : EditMessageReplyMarkup, HasChatId, JsonTelegramCallable<Message>()
}