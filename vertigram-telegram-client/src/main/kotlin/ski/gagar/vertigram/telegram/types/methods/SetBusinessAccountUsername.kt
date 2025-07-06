package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [setBusinessAccountUsername](https://core.telegram.org/bots/api#setbusinessaccountusername) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountUsername internal constructor(
    val businessConnectionId: String,
    val username: String? = null
) : JsonTelegramCallable<Boolean>()
