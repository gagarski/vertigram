package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.richtext.HasRichText
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [editMessageText](https://core.telegram.org/bots/api#editmessagetext) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageText.InlineMessage::class),
    JsonSubTypes.Type(EditMessageText.ChatMessage::class)
)
sealed interface EditMessageText : HasRichText {
    val businessConnectionId: String?
    val linkPreviewOptions: Message.LinkPreviewOptions?
    val replyMarkup: ReplyMarkup?
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageText"
    )
    @TelegramCodegen.Method(
        name = "editMessageText"
    )
    @Throttled
    data class InlineMessage internal constructor(
        override val businessConnectionId: String? = null,
        val inlineMessageId: String,
        override val text: String,
        override val parseMode: RichText.ParseMode? = null,
        override val entities: List<MessageEntity>? = null,
        override val linkPreviewOptions: Message.LinkPreviewOptions? = null,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageText, JsonTelegramCallable<Message>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageText"
    )
    @TelegramCodegen.Method(
        name = "editMessageText"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val businessConnectionId: String? = null,
        override val chatId: ChatId,
        val messageId: Long,
        override val text: String,
        override val parseMode: RichText.ParseMode? = null,
        override val entities: List<MessageEntity>? = null,
        override val linkPreviewOptions: Message.LinkPreviewOptions? = null,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageText, HasChatId, JsonTelegramCallable<Message>()
}