package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.OwnedGifts

/**
 * Returns the gifts owned by a user.
 *
 * See Telegram's [getUserGifts](https://core.telegram.org/bots/api#getusergifts) documentation.
 */
@TelegramCodegen.Method
data class GetUserGifts internal constructor(
    /** Unique identifier of the target user. */
    val userId: Long,
    /** Pass `true` to exclude gifts that can be purchased an unlimited number of times. */
    val excludeUnlimited: Boolean = false,
    /** Pass `true` to exclude limited gifts that can be upgraded to unique. */
    val excludeLimitedUpgradable: Boolean = false,
    /** Pass `true` to exclude limited gifts that can't be upgraded to unique. */
    val excludeLimitedNonUpgradable: Boolean = false,
    /** Pass `true` to exclude gifts assigned from the TON blockchain. */
    val excludeFromBlockchain: Boolean = false,
    /** Pass `true` to exclude unique gifts. */
    val excludeUnique: Boolean = false,
    /** Pass `true` to sort results by gift price instead of send date. */
    val sortByPrice: Boolean = false,
    /** Offset received from the previous request; use an empty string for the first chunk. */
    val offset: String? = null,
    /** Maximum number of gifts to return; 1-100. */
    val limit: Int? = null,
) : JsonTelegramCallable<OwnedGifts>()
