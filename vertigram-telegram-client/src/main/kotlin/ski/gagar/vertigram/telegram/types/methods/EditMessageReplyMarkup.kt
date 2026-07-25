package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit only the reply markup of messages.
 *
 * See Telegram's
 * [editMessageReplyMarkup](https://core.telegram.org/bots/api#editmessagereplymarkup) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageReplyMarkup.InlineMessage::class),
    JsonSubTypes.Type(EditMessageReplyMarkup.ChatMessage::class)
)
sealed interface EditMessageReplyMarkup {
    val businessConnectionId: String?
    val replyMarkup: ReplyMarkup.InlineKeyboard?
    /**
     * Case when the message is an inline message. Returns `true` on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageReplyMarkup"
    )
    @Throttled
    data class InlineMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Identifier of the inline message. */
        val inlineMessageId: String,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageReplyMarkup, JsonTelegramCallable<Boolean>()

    /**
     * Case when the message belongs to a chat. Returns the edited [Message] on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageReplyMarkup"
    )
    @Throttled
    data class ChatMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Identifier of the message to edit. */
        val messageId: Long,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageReplyMarkup, HasChatId, JsonTelegramCallable<Message>()
}
