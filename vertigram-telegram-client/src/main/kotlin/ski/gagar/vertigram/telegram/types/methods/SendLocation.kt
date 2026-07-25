package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.HasReceiverUserId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.SuggestedPost
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Use this method to send a point on the map.
 *
 * See Telegram's [sendLocation](https://core.telegram.org/bots/api#sendlocation) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendLocation internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Identifier of the direct messages topic to which the message will be sent. */
    val directMessagesTopicId: Long? = null,
    /** Latitude of the point. */
    val latitude: Double,
    /** Longitude of the point. */
    val longitude: Double,
    /** Radius of uncertainty for the location, measured in meters. */
    val horizontalAccuracy: Double? = null,
    /** Period for which the location can be updated. */
    val livePeriod: Duration? = null,
    /** Direction in which the user is moving, in degrees. */
    val heading: Int? = null,
    /** Maximum distance for proximity alerts, in meters. */
    val proximityAlertRadius: Int? = null,
    /** Sends the message silently. */
    val disableNotification: Boolean = false,
    /** Protects the sent message from forwarding and saving. */
    val protectContent: Boolean = false,
    /** Pass `true` to allow up to 1000 messages per second for a fee in Telegram Stars. */
    val allowPaidBroadcast: Boolean = false,
    /** Unique identifier of the message effect added to the message. */
    val messageEffectId: String? = null,
    /** Parameters of the suggested post to send. */
    val suggestedPostParameters: SuggestedPost.Parameters? = null,
    /** Unique identifier of the recipient user for an ephemeral message. */
    override val receiverUserId: Long? = null,
    /** Unique identifier of the callback query that allows sending an ephemeral message. */
    val callbackQueryId: String? = null,
    /** Parameters of the message being replied to. */
    val replyParameters: ReplyParameters? = null,
    /** Additional interface options. */
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId, HasReceiverUserId {
    @JsonIgnore
    val isLivePeriodIndefinite = livePeriod?.truncatedTo(ChronoUnit.SECONDS) == DURATION_INDEFINITE

    companion object {
        val DURATION_INDEFINITE: Duration = Duration.ofSeconds(0x7FFFFFFF)
    }
}
