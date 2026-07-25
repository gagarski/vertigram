package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Transfers Telegram Stars from the business account balance to the bot's balance. Returns `true` on success.
 *
 * See Telegram's
 * [transferBusinessAccountStars](https://core.telegram.org/bots/api#transferbusinessaccountstars) documentation.
 */
@TelegramCodegen.Method()
data class TransferBusinessAccountStars internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Number of Telegram Stars to transfer; 1-10000. */
    val starCount: Int
) : JsonTelegramCallable<Boolean>()
