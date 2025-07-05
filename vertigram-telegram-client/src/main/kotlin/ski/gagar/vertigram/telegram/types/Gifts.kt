package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [Gifts](https://core.telegram.org/bots/api#gifts) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Gifts internal constructor(
    val gifts: List<Gift>
) {
    companion object
}
