package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [editMessageMedia](https://core.telegram.org/bots/api#editmessagemedia) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageMedia.InlineMessage::class),
    JsonSubTypes.Type(EditMessageMedia.ChatMessage::class)
)
sealed interface EditMessageMedia {
    val businessConnectionId: String?
    val media: InputMedia
    val replyMarkup: ReplyMarkup.InlineKeyboard?
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageMedia"
    )
    @TelegramCodegen.Method(
        name = "editMessageMedia"
    )
    @Throttled
    data class InlineMessage internal constructor(
        override val businessConnectionId: String? = null,
        val inlineMessageId: String,
        override val media: InputMedia,
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageMedia, MultipartTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageMedia"
    )
    @TelegramCodegen.Method(
        name = "editMessageMedia"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val businessConnectionId: String? = null,
        override val chatId: ChatId,
        val messageId: Long,
        override val media: InputMedia,
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageMedia, HasChatId, MultipartTelegramCallable<Message>()
}