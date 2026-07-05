package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [UserRating](https://core.telegram.org/bots/api#userrating) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class UserRating internal constructor(
    val level: Int,
    val rating: Int,
    val currentLevelRating: Int,
    val nextLevelRating: Int? = null
) {
    companion object
}
