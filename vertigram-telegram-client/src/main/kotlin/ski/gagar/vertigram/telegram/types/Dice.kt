package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [Dice](https://core.telegram.org/bots/api#dice) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Dice internal constructor(
    val emoji: Emoji,
    val value: Int
) {
    enum class Emoji(@JsonValue val emoji: String) {
        DICE("\uD83C\uDFB2"),
        DART("\uD83C\uDFAF"),
        BASKETBALL("\uD83C\uDFC0"),
        FOOTBALL("⚽"),
        BOWLING("\uD83C\uDFB3"),
        SLOT("\uD83C\uDFB0");
    }

    companion object
}
