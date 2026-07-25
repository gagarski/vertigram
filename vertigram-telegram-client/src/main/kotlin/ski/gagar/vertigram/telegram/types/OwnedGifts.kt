package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Contains the gifts owned by a user or a chat.
 *
 * See Telegram's [OwnedGifts](https://core.telegram.org/bots/api#ownedgifts) documentation.
 */
@TelegramCodegen.Type
data class OwnedGifts internal constructor(
    /** Total number of gifts owned by the user or chat. */
    val totalCount: Int,
    /** Requested gifts. */
    val gifts: List<OwnedGift>,
    /** Offset for the next request. */
    val nextOffset: String? = null,
) {
    companion object
}
