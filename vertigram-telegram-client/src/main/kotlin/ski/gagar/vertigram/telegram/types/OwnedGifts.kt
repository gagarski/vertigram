package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [OwnedGifts](https://core.telegram.org/bots/api#ownedgifts) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class OwnedGifts internal constructor(
    val totalCount: Int,
    val gifts: List<OwnedGift>,
    val nextOffset: String? = null,
) {
    companion object
}
