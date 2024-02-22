package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageReplyMarkup](https://core.telegram.org/bots/api#editmessagereplymarkup) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageReplyMarkup.InlineMessage::class),
    JsonSubTypes.Type(EditMessageReplyMarkup.ChatMessage::class)
)
sealed interface EditMessageReplyMarkup {
    val replyMarkup: ReplyMarkup.InlineKeyboard?
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
        val inlineMessageId: String,
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
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
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageReplyMarkup, HasChatId, JsonTelegramCallable<Message>()
}