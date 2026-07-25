package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.REDACTED_SENSITIVE_DATA
import ski.gagar.vertigram.telegram.types.SensitiveResult

/**
 * Replaces the authentication token of a managed bot. Returns the new token on success.
 *
 * See Telegram's [replaceManagedBotToken](https://core.telegram.org/bots/api#replacemanagedbottoken) documentation.
 */
@TelegramCodegen.Method
data class ReplaceManagedBotToken internal constructor(
    /** Unique identifier of the target user whose bot token will be replaced. */
    val userId: Long
) : JsonTelegramCallable<String>(), SensitiveResult {
    override fun withoutSensitiveData(result: Any?) = REDACTED_SENSITIVE_DATA
}
