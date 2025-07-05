package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Telegram [editMessageLiveLocation](https://core.telegram.org/bots/api#editmessagelivelication) method.
 *
 * Subtypes (which are nested) are two mutually exclusive cases: for inline message and for chat message.
 * Note the different return types in these cases.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(EditMessageLiveLocation.InlineMessage::class),
    JsonSubTypes.Type(EditMessageLiveLocation.ChatMessage::class)
)
sealed interface EditMessageLiveLocation {
    val businessConnectionId: String?
    val latitude: Double
    val longitude: Double
    val livePeriod: Duration?
    val horizontalAccuracy: Double?
    val heading: Int?
    val proximityAlertRadius: Int?
    val replyMarkup: ReplyMarkup?

    @get:JsonIgnore
    val isLivePeriodIndefinite
        get() = livePeriod?.truncatedTo(ChronoUnit.SECONDS) == DURATION_INDEFINITE

    /**
     * Inline message case
     */
    @TelegramMethod(
        methodName = "editMessageLiveLocation"
    )
    @TelegramCodegen.Method(
        name = "editMessageLiveLocation",
    )
    @Throttled
    data class InlineMessage internal constructor(
        override val businessConnectionId: String? = null,
        val inlineMessageId: String,
        override val latitude: Double,
        override val longitude: Double,
        override val livePeriod: Duration? = null,
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
    @TelegramCodegen.Method(
        name = "editMessageLiveLocation",
    )
    @Throttled
    data class ChatMessage internal constructor(
        override val businessConnectionId: String? = null,
        override val chatId: ChatId,
        val messageId: Long,
        override val latitude: Double,
        override val longitude: Double,
        override val livePeriod: Duration? = null,
        override val horizontalAccuracy: Double? = null,
        override val heading: Int? = null,
        override val proximityAlertRadius: Int? = null,
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageLiveLocation, HasChatId, JsonTelegramCallable<Message>()

    companion object {
        val DURATION_INDEFINITE = SendLocation.DURATION_INDEFINITE
    }
}