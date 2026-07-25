package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Changes the bio of a managed business account. Returns `true` on success.
 *
 * See Telegram's [setBusinessAccountBio](https://core.telegram.org/bots/api#setbusinessaccountbio) documentation.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountBio internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** New bio for the business account; 0-140 characters. */
    val bio: String? = null
) : JsonTelegramCallable<Boolean>()
