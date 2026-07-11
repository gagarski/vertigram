package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [replaceManagedBotToken](https://core.telegram.org/bots/api#replacemanagedbottoken) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class ReplaceManagedBotToken internal constructor(
    val userId: Long
) : JsonTelegramCallable<String>()
