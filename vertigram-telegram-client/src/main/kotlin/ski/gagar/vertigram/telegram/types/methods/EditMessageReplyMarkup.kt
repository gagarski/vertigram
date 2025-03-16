package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId

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
    @TelegramCodegen.Method(
        name = "editMessageReplyMarkup"
    )
    @Throttled
    data class InlineMessage internal constructor(
        val inlineMessageId: String,
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageReplyMarkup, JsonTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageReplyMarkup"
    )
    @TelegramCodegen.Method(
        name = "editMessageReplyMarkup"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val chatId: ChatId,
        val messageId: Long,
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageReplyMarkup, HasChatId, JsonTelegramCallable<Message>()
}