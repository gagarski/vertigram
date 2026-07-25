package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Describes the rating of a user based on their Telegram Star spendings.
 *
 * See Telegram's [UserRating](https://core.telegram.org/bots/api#userrating) documentation.
 */
@TelegramCodegen.Type
data class UserRating internal constructor(
    /** Current rating level of the user. */
    val level: Int,
    /** Current rating of the user. */
    val rating: Int,
    /** Rating required to reach the current level from the previous level. */
    val currentLevelRating: Int,
    /** Rating required to reach the next level. */
    val nextLevelRating: Int? = null
) {
    companion object
}
