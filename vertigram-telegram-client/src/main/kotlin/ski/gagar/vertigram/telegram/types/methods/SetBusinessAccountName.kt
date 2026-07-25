package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Changes the first and last name of a managed business account. Returns `true` on success.
 *
 * See Telegram's [setBusinessAccountName](https://core.telegram.org/bots/api#setbusinessaccountname) documentation.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountName internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** New first name for the business account; 1-64 characters. */
    val firstName: String,
    /** New last name for the business account; 0-64 characters. */
    val lastName: String? = null
) : JsonTelegramCallable<Boolean>()
