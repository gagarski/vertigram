package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [upgradeGift](https://core.telegram.org/bots/api#upgradeGift) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class UpgradeGift internal constructor(
    val businessConnectionId: String,
    val ownedGiftId: String,
    val keepOriginalDetails: Boolean = false,
    val starCount: Int? = null
) : JsonTelegramCallable<Boolean>()
