package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Use this method to edit live location messages.
 *
 * A location can be edited until its [livePeriod] expires or editing is explicitly disabled by calling
 * [ski.gagar.vertigram.telegram.client.Telegram.stopMessageLiveLocation].
 *
 * See Telegram's
 * [editMessageLiveLocation](https://core.telegram.org/bots/api#editmessagelivelocation) documentation.
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
     * Case when the message is an inline message. Returns `true` on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageLiveLocation",
    )
    @Throttled
    data class InlineMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Identifier of the inline message. */
        val inlineMessageId: String,
        /** New latitude of the location. */
        override val latitude: Double,
        /** New longitude of the location. */
        override val longitude: Double,
        /** New period for which the location can be updated. */
        override val livePeriod: Duration? = null,
        /** Radius of uncertainty for the location, measured in meters; 0-1500. */
        override val horizontalAccuracy: Double? = null,
        /** Direction in which the user is moving, in degrees; 1-360. */
        override val heading: Int? = null,
        /** Maximum distance for proximity alerts, in meters; 1-100000. */
        override val proximityAlertRadius: Int? = null,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageLiveLocation, JsonTelegramCallable<Boolean>()

    /**
     * Case when the message belongs to a chat. Returns the edited [Message] on success.
     */
    @TelegramCodegen.Method(
        name = "editMessageLiveLocation",
    )
    @Throttled
    data class ChatMessage internal constructor(
        /** Unique identifier of the business connection on behalf of which the message was sent. */
        override val businessConnectionId: String? = null,
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Identifier of the message to edit. */
        val messageId: Long,
        /** New latitude of the location. */
        override val latitude: Double,
        /** New longitude of the location. */
        override val longitude: Double,
        /** New period for which the location can be updated. */
        override val livePeriod: Duration? = null,
        /** Radius of uncertainty for the location, measured in meters; 0-1500. */
        override val horizontalAccuracy: Double? = null,
        /** Direction in which the user is moving, in degrees; 1-360. */
        override val heading: Int? = null,
        /** Maximum distance for proximity alerts, in meters; 1-100000. */
        override val proximityAlertRadius: Int? = null,
        /** New inline keyboard for the message. */
        override val replyMarkup: ReplyMarkup? = null
    ) : EditMessageLiveLocation, HasChatId, JsonTelegramCallable<Message>()

    companion object {
        val DURATION_INDEFINITE = SendLocation.DURATION_INDEFINITE
    }
}
