package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.LocalDate
import java.time.MonthDay

/**
 * Telegram [Birthdate](https://core.telegram.org/bots/api#birthdate) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed interface Birthdate {
    @TelegramCodegen.Type
    data class Full internal constructor(
        val day: Int,
        val month: Int,
        val year: Int
    ) : Birthdate {
        fun toLocalDate(): LocalDate = LocalDate.of(year, month, day)

        companion object
    }

    @TelegramCodegen.Type
    data class MonthDay internal constructor(
        val day: Int,
        val month: Int
    ) : Birthdate {
        fun toMonthDay(): java.time.MonthDay = java.time.MonthDay.of(month, day)

        companion object
    }

    fun from(localDate: LocalDate) = Full(day = localDate.dayOfMonth, month = localDate.month.value, year = localDate.year)
    fun from(monthDay: java.time.MonthDay) = MonthDay(day = monthDay.dayOfMonth, month = monthDay.month.value)
}