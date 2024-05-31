package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import ski.gagar.vertigram.telegram.types.BusinessOpeningHours
import ski.gagar.vertigram.telegram.types.BusinessOpeningHours.OpeningTime
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Deserializes [BusinessOpeningHours.OpeningTime] as minute's sequence number, used in [TELEGRAM_JSON_MAPPER].
 */
internal class OpeningTimeDeserializer : JsonDeserializer<OpeningTime>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): OpeningTime {
        return deserializeOpeningTime(parser.intValue)
    }

    override fun handledType(): Class<OpeningTime> = OpeningTime::class.java
}

const val MAX_INT_VALUE = 7 * 24 * 60 - 1

fun deserializeOpeningTime(value: Int): OpeningTime {
    require(value <= MAX_INT_VALUE)
    val dow = value / (24 * 60) + 1
    val hhmm = value % (24 * 60)
    val hh = hhmm / 60
    val mm = hhmm % 60
    return OpeningTime(LocalTime.of(hh, mm), DayOfWeek.of(dow))
}