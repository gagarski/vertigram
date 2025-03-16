package ski.gagar.vertigram.telegram.types.methods

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
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageCaption](https://core.telegram.org/bots/api#editmessagecaption) method.

 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageCaption.InlineMessage::class),
    JsonSubTypes.Type(EditMessageCaption.ChatMessage::class)
)
sealed interface EditMessageCaption : HasOptionalRichCaption {
    val businessConnectionId: String?
    val replyMarkup: ReplyMarkup?
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageCaption"
    )
    @TelegramCodegen.Method(
        name = "editMessageCaption"
    )
    @Throttled
    data class InlineMessage internal constructor(
        override val businessConnectionId: String? = null,
        val inlineMessageId: String,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageCaption, JsonTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageCaption"
    )
    @TelegramCodegen.Method(
        name = "editMessageCaption"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val businessConnectionId: String? = null,
        override val chatId: ChatId,
        val messageId: Long,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageCaption, HasChatId, JsonTelegramCallable<Message>()
}