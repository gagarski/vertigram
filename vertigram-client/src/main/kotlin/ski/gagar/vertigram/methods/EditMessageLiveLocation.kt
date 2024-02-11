package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageLiveLocation](https://core.telegram.org/bots/api#editmessagelivelication) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface EditMessageLiveLocation {
    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageLiveLocation"
    )
    @TelegramCodegen(
        methodName = "editMessageLiveLocation",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageLiveLocation"
    )
    @Throttled
    data class InlineMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val inlineMessageId: String,
        val latitude: Double,
        val longitude: Double,
        val horizontalAccuracy: Double? = null,
        val heading: Int? = null,
        val proximityAlertRadius: Int? = null,
        val replyMarkup: ReplyMarkup? = null
    ) : EditMessageLiveLocation, JsonTelegramCallable<Boolean>()

    /**
     * Chat message case
     */
    @TelegramMethod(
        methodName = "editMessageLiveLocation"
    )
    @TelegramCodegen(
        methodName = "editMessageLiveLocation",
        generatePseudoConstructor = true,
        pseudoConstructorName = "EditMessageLiveLocation"
    )
    @Throttled
    data class ChatMessage internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val chatId: ChatId,
        val messageId: Long,
        val latitude: Double,
        val longitude: Double,
        val horizontalAccuracy: Double? = null,
        val heading: Int? = null,
        val proximityAlertRadius: Int? = null,
        val replyMarkup: ReplyMarkup? = null
    ) : EditMessageCaption, HasChatId, JsonTelegramCallable<Message>()
}