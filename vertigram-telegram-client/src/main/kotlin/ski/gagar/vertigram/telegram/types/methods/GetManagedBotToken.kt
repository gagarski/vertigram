package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.REDACTED_SENSITIVE_DATA
import ski.gagar.vertigram.telegram.types.SensitiveResult

/**
 * Returns the authentication token of a managed bot.
 *
 * See Telegram's [getManagedBotToken](https://core.telegram.org/bots/api#getmanagedbottoken) documentation.
 */
@TelegramCodegen.Method
data class GetManagedBotToken internal constructor(
    /** Unique identifier of the target user whose bot token will be returned. */
    val userId: Long
) : JsonTelegramCallable<String>(), SensitiveResult {
    override fun withoutSensitiveData(result: Any?) = REDACTED_SENSITIVE_DATA
}
