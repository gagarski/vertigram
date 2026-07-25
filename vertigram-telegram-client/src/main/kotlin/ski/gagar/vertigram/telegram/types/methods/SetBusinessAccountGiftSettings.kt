package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.AcceptedGiftTypes

/**
 * Changes the privacy settings pertaining to gifts for a managed business account. Returns `true` on success.
 *
 * See Telegram's
 * [setBusinessAccountGiftSettings](https://core.telegram.org/bots/api#setbusinessaccountgiftsettings) documentation.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountGiftSettings internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Pass `true` to show the gift button in the account's profile. */
    val showGiftButton: Boolean,
    /** Types of gifts accepted by the business account. */
    val acceptedGiftTypes: AcceptedGiftTypes
) : JsonTelegramCallable<Boolean>()
