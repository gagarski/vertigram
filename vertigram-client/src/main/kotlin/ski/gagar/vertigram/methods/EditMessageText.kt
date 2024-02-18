package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.LinkPreviewOptions
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.richtext.HasRichText
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageText](https://core.telegram.org/bots/api#editmessagetext) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface EditMessageText : HasRichText {
    val linkPreviewOptions: LinkPreviewOptions?
    val replyMarkup: ReplyMarkup?
    /**
     * Inline message case
     */
    @TelegramMethod(
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
        val inlineMessageId: Long,
        override val text: String,
        override val parseMode: ParseMode? = null,
        override val entities: List<MessageEntity>? = null,
        override val linkPreviewOptions: LinkPreviewOptions? = null,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageText, JsonTelegramCallable<Message>()

    /**
     * Chat message case
     */
    @TelegramMethod(
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
        override val parseMode: ParseMode? = null,
        override val entities: List<MessageEntity>? = null,
        override val linkPreviewOptions: LinkPreviewOptions? = null,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageText, HasChatId, JsonTelegramCallable<Message>()
}