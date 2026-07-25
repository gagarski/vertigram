package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.LocalDate
import java.time.MonthDay

/**
 * Describes the birthdate of a user.
 *
 * See Telegram's [Birthdate](https://core.telegram.org/bots/api#birthdate) documentation.
 */
sealed interface Birthdate {
    /**
     * Describes the birthdate of a user when the year is known.
     *
     * See Telegram's [Birthdate](https://core.telegram.org/bots/api#birthdate) documentation.
     */
    @TelegramCodegen.Type
    data class Full internal constructor(
        /** Day of the user's birth; 1-31. */
        val day: Int,
        /** Month of the user's birth; 1-12. */
        val month: Int,
        /** Year of the user's birth. */
        val year: Int
    ) : Birthdate {
        fun toLocalDate(): LocalDate = LocalDate.of(year, month, day)

        companion object
    }

    /**
     * Describes the birthdate of a user when the year is unknown.
     *
     * See Telegram's [Birthdate](https://core.telegram.org/bots/api#birthdate) documentation.
     */
    @TelegramCodegen.Type
    data class MonthDay internal constructor(
        /** Day of the user's birth; 1-31. */
        val day: Int,
        /** Month of the user's birth; 1-12. */
        val month: Int
    ) : Birthdate {
        fun toMonthDay(): java.time.MonthDay = java.time.MonthDay.of(month, day)

        companion object
    }

    fun from(localDate: LocalDate) = Full(day = localDate.dayOfMonth, month = localDate.month.value, year = localDate.year)
    fun from(monthDay: java.time.MonthDay) = MonthDay(day = monthDay.dayOfMonth, month = monthDay.month.value)
}
