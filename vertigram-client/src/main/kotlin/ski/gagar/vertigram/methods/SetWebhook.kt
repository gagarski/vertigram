package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.types.UpdateType
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setWebhook](https://core.telegram.org/bots/api#setwebhook) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetWebhook(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val url: String,
    @TelegramMedia
    val certificate: Attachment? = null,
    val ipAddress: String? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<UpdateType>? = null,
    val dropPendingUpdates: Boolean = false,
    val secretToken: String? = null
) : MultipartTelegramCallable<Boolean>()
