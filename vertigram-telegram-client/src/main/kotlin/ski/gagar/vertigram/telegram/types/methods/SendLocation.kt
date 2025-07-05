package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Telegram [sendLocation](https://core.telegram.org/bots/api#sendlocation) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class SendLocation internal constructor(
    val businessConnectionId: String? = null,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Duration? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val allowPaidBroadcast: Boolean = false,
    val messageEffectId: String? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId {
    @JsonIgnore
    val isLivePeriodIndefinite = livePeriod?.truncatedTo(ChronoUnit.SECONDS) == DURATION_INDEFINITE

    companion object {
        val DURATION_INDEFINITE: Duration = Duration.ofSeconds(0x7FFFFFFF)
    }
}
