package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * This object represents a list of gifts.
 *
 * See Telegram's [Gifts](https://core.telegram.org/bots/api#gifts) documentation.
 */
@TelegramCodegen.Type
data class Gifts internal constructor(
    /** The list of gifts. */
    val gifts: List<Gift>
) {
    companion object
}
