package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [sendLocation](https://core.telegram.org/bots/api#sendlocation) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SendLocation(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
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
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Message>(), HasChatId
