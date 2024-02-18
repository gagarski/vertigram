package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editMessageLiveLocation](https://core.telegram.org/bots/api#editmessagelivelication) method.
 *
 * Subtypes (which are nested) are two mutually-exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface EditMessageLiveLocation {
    val latitude: Double
    val longitude: Double
    val horizontalAccuracy: Double?
    val heading: Int?
    val proximityAlertRadius: Int?
    val replyMarkup: ReplyMarkup?
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
        override val latitude: Double,
        override val longitude: Double,
        override val horizontalAccuracy: Double? = null,
        override val heading: Int? = null,
        override val proximityAlertRadius: Int? = null,
        override val replyMarkup: ReplyMarkup? = null
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
        override val latitude: Double,
        override val longitude: Double,
        override val horizontalAccuracy: Double? = null,
        override val heading: Int? = null,
        override val proximityAlertRadius: Int? = null,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageLiveLocation, HasChatId, JsonTelegramCallable<Message>()
}