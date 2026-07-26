package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.InputRichMessage
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.formattedtext.HasFormattedText
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit text, rich and [ski.gagar.vertigram.telegram.types.Game] messages.
 *
 * See Telegram's [editMessageText](https://core.telegram.org/bots/api#editmessagetext) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageText.InlineMessage::class),
    JsonSubTypes.Type(EditMessageText.ChatMessage::class)
)
sealed interface EditMessageText : HasFormattedText {
    val businessConnectionId: String?
    val linkPreviewOptions: Message.LinkPreviewOptions?
    val richMessage: InputRichMessage?
    val replyMarkup: ReplyMarkup?
    /**
     * Case when the message is an inline message. Returns `true` on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageText"
    )
    @Throttled
    data class InlineMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Identifier of the inline message. */
        val inlineMessageId: String,
        /** New text of the message, 1-4096 characters after entities parsing. */
        override val text: String,
        /** Mode for parsing entities in [text]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [text]; can be specified instead of [parseMode]. */
        override val entities: List<MessageEntity>? = null,
        /** Link preview generation options for the message. */
        override val linkPreviewOptions: Message.LinkPreviewOptions? = null,
        /** Rich message content. */
        override val richMessage: InputRichMessage? = null,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageText, MultipartTelegramCallable<Message>()

    /**
     * Case when the message belongs to a chat. Returns the edited [Message] on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageText"
    )
    @Throttled
    data class ChatMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Identifier of the message to edit. */
        val messageId: Long,
        /** New text of the message, 1-4096 characters after entities parsing. */
        override val text: String,
        /** Mode for parsing entities in [text]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [text]; can be specified instead of [parseMode]. */
        override val entities: List<MessageEntity>? = null,
        /** Link preview generation options for the message. */
        override val linkPreviewOptions: Message.LinkPreviewOptions? = null,
        /** Rich message content. */
        override val richMessage: InputRichMessage? = null,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageText, HasChatId, MultipartTelegramCallable<Message>()
}
