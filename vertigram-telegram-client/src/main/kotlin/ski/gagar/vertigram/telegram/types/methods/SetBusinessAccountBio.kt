package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [setBusinessAccountBio](https://core.telegram.org/bots/api#setbusinessaccountbio) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountBio internal constructor(
    val businessConnectionId: String,
    val bio: String? = null
) : JsonTelegramCallable<Boolean>()
