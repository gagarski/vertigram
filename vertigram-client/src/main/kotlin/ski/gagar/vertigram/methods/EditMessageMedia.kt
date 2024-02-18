package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.InputMedia
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageMedia](https://core.telegram.org/bots/api#editmessagemedia) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface EditMessageMedia {
    val media: InputMedia
    val replyMarkup: ReplyMarkup.InlineKeyboard?
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageMedia"
    )
    @TelegramCodegen(
        methodName = "editMessageMedia",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageMedia"
    )
    @Throttled
    data class InlineMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inlineMessageId: Long,
        @TelegramMedia
        override val media: InputMedia,
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageMedia, MultipartTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageMedia"
    )
    @TelegramCodegen(
        methodName = "editMessageMedia",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageMedia"
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val chatId: ChatId,
        val messageId: Long,
        @TelegramMedia
        override val media: InputMedia,
        override val replyMarkup: ReplyMarkup.InlineKeyboard? = null
    ) : EditMessageMedia, HasChatId, MultipartTelegramCallable<Message>()
}