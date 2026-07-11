package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [getManagedBotToken](https://core.telegram.org/bots/api#getmanagedbottoken) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetManagedBotToken internal constructor(
    val userId: Long
) : JsonTelegramCallable<String>()
