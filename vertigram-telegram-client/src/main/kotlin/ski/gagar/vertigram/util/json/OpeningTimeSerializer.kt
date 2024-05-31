package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import ski.gagar.vertigram.telegram.types.BusinessOpeningHours

/**
 * Serializes [BusinessOpeningHours.OpeningTime] in minute's sequence number,
 * used in [TELEGRAM_JSON_MAPPER].
 */
internal class OpeningTimeSerializer : JsonSerializer<BusinessOpeningHours.OpeningTime>() {
    override fun serialize(time: BusinessOpeningHours.OpeningTime, gen: JsonGenerator, sp: SerializerProvider?) {
        gen.writeNumber(time.intValue)
    }

    override fun handledType(): Class<BusinessOpeningHours.OpeningTime> = BusinessOpeningHours.OpeningTime::class.java
}

val BusinessOpeningHours.OpeningTime.intValue
    get() = (dayOfWeek.value - 1) * 24 * 60 + time.hour * 60 + time.minute