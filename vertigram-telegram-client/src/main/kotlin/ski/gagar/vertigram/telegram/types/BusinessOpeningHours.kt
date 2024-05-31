package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Telegram [BusinessOpeningHours](https://core.telegram.org/bots/api#businessopeninghours) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class BusinessOpeningHours(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val timeZoneName: String,
    val openingHours: List<Interval>
) {
    /**
     * Telegram [BusinessOpeningHoursInterval](https://core.telegram.org/bots/api#businessopeninghoursinterval) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Interval @Deprecated("Use factory function instead") constructor(
        val openingMinute: OpeningTime,
        val closingMinute: OpeningTime
    )

    data class OpeningTime(
        val time: LocalTime,
        val dayOfWeek: DayOfWeek
    ) {

    }
}
