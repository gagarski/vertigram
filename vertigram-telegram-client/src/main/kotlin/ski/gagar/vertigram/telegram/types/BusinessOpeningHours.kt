package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Describes the opening hours of a business.
 *
 * See Telegram's [BusinessOpeningHours](https://core.telegram.org/bots/api#businessopeninghours) documentation.
 */
@TelegramCodegen.Type
data class BusinessOpeningHours internal constructor(
    /** Unique name of the time zone for which the opening hours are defined. */
    val timeZoneName: String,
    /** List of time intervals describing business opening hours. */
    val openingHours: List<Interval>
) {
    /**
     * Describes an interval of time during which a business is open.
     *
     * See Telegram's
     * [BusinessOpeningHoursInterval](https://core.telegram.org/bots/api#businessopeninghoursinterval) documentation.
     */
    @TelegramCodegen.Type
    data class Interval internal constructor(
        /** Start of the time interval during which the business is open. */
        val openingMinute: OpeningTime,
        /** End of the time interval during which the business is open. */
        val closingMinute: OpeningTime
    ) {
        companion object
    }

    /**
     * Kotlin representation of a minute's sequence number in a week.
     *
     * See Telegram's
     * [BusinessOpeningHoursInterval](https://core.telegram.org/bots/api#businessopeninghoursinterval) documentation.
     */
    @TelegramCodegen.Type
    data class OpeningTime internal constructor(
        /** Local time of the interval boundary. */
        val time: LocalTime,
        /** Day of the week of the interval boundary. */
        val dayOfWeek: DayOfWeek
    ) {
        companion object
    }
    companion object
}
