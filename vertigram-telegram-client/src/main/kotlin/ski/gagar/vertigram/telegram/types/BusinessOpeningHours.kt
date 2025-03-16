package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Telegram [BusinessOpeningHours](https://core.telegram.org/bots/api#businessopeninghours) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BusinessOpeningHours internal constructor(
    val timeZoneName: String,
    val openingHours: List<Interval>
) {
    /**
     * Telegram [BusinessOpeningHoursInterval](https://core.telegram.org/bots/api#businessopeninghoursinterval) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Interval internal constructor(
        val openingMinute: OpeningTime,
        val closingMinute: OpeningTime
    ) {
        companion object
    }

    @TelegramCodegen.Type
    data class OpeningTime internal constructor(
        val time: LocalTime,
        val dayOfWeek: DayOfWeek
    ) {
        companion object
    }
    companion object
}
