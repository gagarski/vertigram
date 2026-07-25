package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Changes the username of a managed business account. Returns `true` on success.
 *
 * See Telegram's
 * [setBusinessAccountUsername](https://core.telegram.org/bots/api#setbusinessaccountusername) documentation.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountUsername internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** New username for the business account; 0-32 characters. */
    val username: String? = null
) : JsonTelegramCallable<Boolean>()
