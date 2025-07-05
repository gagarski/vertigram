package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Telegram [WebhookInfo](https://core.telegram.org/bots/api#webhookinfo) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class WebhookInfo internal constructor(
    val url: String,
    val hasCustomCertificate: Boolean = false,
    val pendingUpdateCount: Int,
    val ipAddress: String? = null,
    val lastErrorDate: Instant? = null,
    val lastErrorMessage: String? = null,
    val lastSynchronizationErrorDate: Instant? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<String>? = null
) {
    companion object
}