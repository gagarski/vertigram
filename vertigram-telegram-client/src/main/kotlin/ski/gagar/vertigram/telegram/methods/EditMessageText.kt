package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
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
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageText](https://core.telegram.org/bots/api#editmessagetext) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageText.InlineMessage::class),
    JsonSubTypes.Type(EditMessageText.ChatMessage::class)
)
sealed interface EditMessageText : HasRichText {
    val linkPreviewOptions: Message.LinkPreviewOptions?
    val replyMarkup: ReplyMarkup?
    /**
     * Inline message case
     */
    @ski.gagar.vertigram.telegram.annotations.TelegramMethod(
        methodName = "editMessageText"
    )
    @TelegramCodegen(
        methodName = "editMessageText",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageText"
    )
    @Throttled
    data class InlineMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
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
    @ski.gagar.vertigram.telegram.annotations.TelegramMethod(
        methodName = "editMessageText"
    )
    @TelegramCodegen(
        methodName = "editMessageText",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageText"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val chatId: ChatId,
        val messageId: Long,
        override val text: String,
        override val parseMode: RichText.ParseMode? = null,
        override val entities: List<MessageEntity>? = null,
        override val linkPreviewOptions: Message.LinkPreviewOptions? = null,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageText, HasChatId, JsonTelegramCallable<Message>()
}