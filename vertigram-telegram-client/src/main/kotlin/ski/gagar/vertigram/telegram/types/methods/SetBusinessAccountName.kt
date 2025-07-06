package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [setBusinessAccountName](https://core.telegram.org/bots/api#setbusinessaccountname) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountName internal constructor(
    val businessConnectionId: String,
    val firstName: String,
    val lastName: String? = null
) : JsonTelegramCallable<Boolean>()
