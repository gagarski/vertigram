package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedCaption
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to edit captions of messages.
 *
 * See Telegram's [editMessageCaption](https://core.telegram.org/bots/api#editmessagecaption) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageCaption.InlineMessage::class),
    JsonSubTypes.Type(EditMessageCaption.ChatMessage::class)
)
sealed interface EditMessageCaption : HasOptionalFormattedCaption {
    val businessConnectionId: String?
    val replyMarkup: ReplyMarkup?
    /**
     * Case when the message is an inline message. Returns `true` on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageCaption"
    )
    @Throttled
    data class InlineMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Identifier of the inline message. */
        val inlineMessageId: String,
        /** New caption, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in the new caption. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in the caption; can be specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Pass `true` to show the caption above the message media. */
        val showCaptionAboveMedia: Boolean = false,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageCaption, JsonTelegramCallable<Boolean>()

    /**
     * Case when the message belongs to a chat. Returns the edited [Message] on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageCaption"
    )
    @Throttled
    data class ChatMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Identifier of the message to edit. */
        val messageId: Long,
        /** New caption, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in the new caption. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in the caption; can be specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Pass `true` to show the caption above the message media. */
        val showCaptionAboveMedia: Boolean = false,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageCaption, HasChatId, JsonTelegramCallable<Message>()
}
