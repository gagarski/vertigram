package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [AcceptedGiftTypes](https://core.telegram.org/bots/api#acceptedgifttypes) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class AcceptedGiftTypes internal constructor(
    val unlimitedGifts: Boolean,
    val limitedGifts: Boolean,
    val uniqueGifts: Boolean,
    val premiumSubscription: Boolean
) {
    companion object
}
