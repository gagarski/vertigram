package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.REDACTED_SENSITIVE_DATA
import ski.gagar.vertigram.telegram.types.SensitiveData
import ski.gagar.vertigram.telegram.types.attachments.Attachment

/**
 * Use this method to specify a URL and receive incoming updates via an outgoing webhook. Returns `true` on success.
 *
 * See Telegram's [setWebhook](https://core.telegram.org/bots/api#setwebhook) documentation.
 */
@TelegramCodegen.Method
data class SetWebhook internal constructor(
    /** HTTPS URL to send updates to. */
    val url: String,
    /** Public key certificate used to check the webhook certificate. */
    val certificate: Attachment? = null,
    /** Fixed IP address used to send webhook requests instead of the address resolved through DNS. */
    val ipAddress: String? = null,
    /** Maximum number of simultaneous HTTPS connections for update delivery; 1-100. */
    val maxConnections: Int? = null,
    /** Update types the bot wants to receive. */
    val allowedUpdates: List<Update.Type>? = null,
    /** Pass `true` to drop all pending updates. */
    val dropPendingUpdates: Boolean = false,
    /** Secret token sent in the `X-Telegram-Bot-Api-Secret-Token` header. */
    val secretToken: String? = null
) : MultipartTelegramCallable<Boolean>(), SensitiveData<SetWebhook> {
    override fun copyWithoutSensitiveData() =
        copy(secretToken = secretToken?.let { REDACTED_SENSITIVE_DATA })
}
