package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [declineSuggestedPost](https://core.telegram.org/bots/api#declinesuggestedpost) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class DeclineSuggestedPost internal constructor(
    val chatId: Long,
    val messageId: Long,
    val comment: String? = null
) : JsonTelegramCallable<Boolean>()
