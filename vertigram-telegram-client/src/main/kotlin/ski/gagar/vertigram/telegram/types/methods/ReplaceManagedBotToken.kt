package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.REDACTED_SENSITIVE_DATA
import ski.gagar.vertigram.telegram.types.SensitiveResult

/**
 * Telegram [replaceManagedBotToken](https://core.telegram.org/bots/api#replacemanagedbottoken) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ReplaceManagedBotToken internal constructor(
    val userId: Long
) : JsonTelegramCallable<String>(), SensitiveResult {
    override fun withoutSensitiveData(result: Any?) = REDACTED_SENSITIVE_DATA
}
