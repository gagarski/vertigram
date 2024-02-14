package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageCaption](https://core.telegram.org/bots/api#editmessagecaption) method.

 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface EditMessageCaption {
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageCaption"
    )
    @TelegramCodegen(
        methodName = "editMessageCaption",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageCaption"
    )
    @Throttled
    data class InlineMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inlineMessageId: Long,
        @PublishedApi internal val caption: String? = null,
        @PublishedApi internal val parseMode: ParseMode? = null,
        @PublishedApi internal val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: ReplyMarkup? = null
    ) : EditMessageCaption, JsonTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageCaption"
    )
    @TelegramCodegen(
        methodName = "editMessageCaption",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageCaption"
    )
    @Throttled
    data class ChatMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        val messageId: Long,
        @PublishedApi internal val caption: String? = null,
        @PublishedApi internal val parseMode: ParseMode? = null,
        @PublishedApi internal val captionEntities: List<MessageEntity>? = null,
        val replyMarkup: ReplyMarkup? = null
    ) : EditMessageCaption, HasChatId, JsonTelegramCallable<Message>()
}