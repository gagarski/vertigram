package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Describes the current status of a webhook.
 *
 * See Telegram's [WebhookInfo](https://core.telegram.org/bots/api#webhookinfo) documentation.
 */
@TelegramCodegen.Type
data class WebhookInfo internal constructor(
    /** Webhook URL, empty when webhook mode is not used. */
    val url: String,
    /** Whether a custom certificate was provided for webhook certificate checks. */
    val hasCustomCertificate: Boolean = false,
    /** Number of updates awaiting delivery. */
    val pendingUpdateCount: Int,
    /** Current webhook IP address. */
    val ipAddress: String? = null,
    /** Date when an error last happened while delivering an update. */
    val lastErrorDate: Instant? = null,
    /** Error message for the last error while delivering an update. */
    val lastErrorMessage: String? = null,
    /** Date of the last synchronization error. */
    val lastSynchronizationErrorDate: Instant? = null,
    /** Maximum number of simultaneous HTTPS connections for update delivery. */
    val maxConnections: Int? = null,
    /** Update types the bot is subscribed to. */
    val allowedUpdates: List<String>? = null
) {
    companion object
}
