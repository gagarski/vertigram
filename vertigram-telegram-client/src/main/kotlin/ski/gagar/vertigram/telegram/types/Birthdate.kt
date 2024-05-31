package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate

/**
 * Telegram [Birthdate](https://core.telegram.org/bots/api#birthdate) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Birthdate(
    val day: Int,
    val month: Int,
    val year: Int? = null
) {
    /**
     * Return `this` as a local date if the [year] is present
     */
    @get:JsonIgnore
    val localDate: LocalDate?
        get() = year?.let { LocalDate.of(year, month, day) }
}
