package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.attachments.Attachment

/**
 * Telegram [setWebhook](https://core.telegram.org/bots/api#setwebhook) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetWebhook internal constructor(
    val url: String,
    val certificate: Attachment? = null,
    val ipAddress: String? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<Update.Type>? = null,
    val dropPendingUpdates: Boolean = false,
    val secretToken: String? = null
) : MultipartTelegramCallable<Boolean>()
