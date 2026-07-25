package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * This object describes the types of gifts that can be gifted to a user or a chat.
 *
 * See Telegram's [AcceptedGiftTypes](https://core.telegram.org/bots/api#acceptedgifttypes) documentation.
 */
@TelegramCodegen.Type
data class AcceptedGiftTypes internal constructor(
    /** `true` if unlimited regular gifts are accepted. */
    val unlimitedGifts: Boolean,
    /** `true` if limited regular gifts are accepted. */
    val limitedGifts: Boolean,
    /** `true` if unique gifts or gifts that can be upgraded to unique for free are accepted. */
    val uniqueGifts: Boolean,
    /** `true` if a Telegram Premium subscription is accepted. */
    val premiumSubscription: Boolean,
    /** `true` if transfers of unique gifts from channels are accepted. */
    val giftsFromChannels: Boolean = false
) {
    companion object
}
