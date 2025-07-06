package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.AcceptedGiftTypes

/**
 * Telegram [setBusinessAccountGiftSettings](https://core.telegram.org/bots/api#setbusinessaccountgiftsettings) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class SetBusinessAccountGiftSettings internal constructor(
    val businessConnectionId: String,
    val showGiftButton: Boolean,
    val acceptedGiftTypes: AcceptedGiftTypes
) : JsonTelegramCallable<Boolean>()
