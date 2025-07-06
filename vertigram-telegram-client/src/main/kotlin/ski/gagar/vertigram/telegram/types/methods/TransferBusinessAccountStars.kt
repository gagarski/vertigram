package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [transferBusinessAccountStars](https://core.telegram.org/bots/api#transferbusinessaccountstars) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class TransferBusinessAccountStars internal constructor(
    val businessConnectionId: String,
    val starCount: Int
) : JsonTelegramCallable<Boolean>()
