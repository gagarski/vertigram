package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.OwnedGifts

/**
 * Telegram [getUserGifts](https://core.telegram.org/bots/api#getusergifts) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetUserGifts internal constructor(
    val userId: Long,
    val excludeUnlimited: Boolean = false,
    val excludeLimitedUpgradable: Boolean = false,
    val excludeLimitedNonUpgradable: Boolean = false,
    val excludeFromBlockchain: Boolean = false,
    val excludeUnique: Boolean = false,
    val sortByPrice: Boolean = false,
    val offset: String? = null,
    val limit: Int? = null,
) : JsonTelegramCallable<OwnedGifts>()
