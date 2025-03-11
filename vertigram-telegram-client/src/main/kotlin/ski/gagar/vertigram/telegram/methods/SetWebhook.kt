package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setWebhook](https://core.telegram.org/bots/api#setwebhook) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class SetWebhook(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val url: String,
    @TelegramMedia
    val certificate: Attachment? = null,
    val ipAddress: String? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<Update.Type>? = null,
    val dropPendingUpdates: Boolean = false,
    val secretToken: String? = null
) : MultipartTelegramCallable<Boolean>()
