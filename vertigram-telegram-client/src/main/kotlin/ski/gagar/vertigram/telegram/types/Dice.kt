package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents an animated emoji that displays a random value.
 *
 * See Telegram's [Dice](https://core.telegram.org/bots/api#dice) documentation.
 */
@TelegramCodegen.Type
data class Dice internal constructor(
    /** Emoji on which the dice throw animation is based. */
    val emoji: Emoji,
    /** Value of the dice, whose range depends on [emoji]. */
    val value: Int
) {
    enum class Emoji(@JsonValue val emoji: String) {
        /** Dice emoji; values range from 1 to 6. */
        DICE("\uD83C\uDFB2"),
        /** Dartboard emoji; values range from 1 to 6. */
        DART("\uD83C\uDFAF"),
        /** Basketball emoji; values range from 1 to 5. */
        BASKETBALL("\uD83C\uDFC0"),
        /** Football emoji; values range from 1 to 5. */
        FOOTBALL("⚽"),
        /** Bowling emoji; values range from 1 to 6. */
        BOWLING("\uD83C\uDFB3"),
        /** Slot machine emoji; values range from 1 to 64. */
        SLOT("\uD83C\uDFB0");
    }

    companion object
}
