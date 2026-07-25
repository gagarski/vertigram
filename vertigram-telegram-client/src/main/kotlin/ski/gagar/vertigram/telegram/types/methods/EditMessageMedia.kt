package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit animation, audio, document, photo, live photo or video messages, or to add media to text
 * messages.
 *
 * See Telegram's [editMessageMedia](https://core.telegram.org/bots/api#editmessagemedia) documentation.
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
     * Case when the message is an inline message. Returns `true` on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageMedia"
    )
    @Throttled
    data class InlineMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Identifier of the inline message. */
        val inlineMessageId: String,
        /** New media content of the message. */
        override val media: InputMedia,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageMedia, MultipartTelegramCallable<Boolean>()

    /**
     * Case when the message belongs to a chat. Returns the edited [Message] on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageMedia"
    )
    @Throttled
    data class ChatMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Identifier of the message to edit. */
        val messageId: Long,
        /** New media content of the message. */
        override val media: InputMedia,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageMedia, HasChatId, MultipartTelegramCallable<Message>()
}
