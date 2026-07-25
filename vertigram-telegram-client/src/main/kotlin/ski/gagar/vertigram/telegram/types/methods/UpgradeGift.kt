package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Upgrades a given regular gift to a unique gift. Returns `true` on success.
 *
 * See Telegram's [upgradeGift](https://core.telegram.org/bots/api#upgradegift) documentation.
 */
@TelegramCodegen.Method()
data class UpgradeGift internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Unique identifier of the regular gift to upgrade. */
    val ownedGiftId: String,
    /** Pass `true` to keep the original gift text, sender and receiver in the upgraded gift. */
    val keepOriginalDetails: Boolean = false,
    /** Number of Telegram Stars paid for the upgrade from the business account balance. */
    val starCount: Int? = null
) : JsonTelegramCallable<Boolean>()
