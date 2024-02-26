package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.Instant

/**
 * Serializes [Instant] as UNIX time in seconds, used in [TELEGRAM_JSON_MAPPER].
 */
internal class UnixTimestampSerializer : JsonSerializer<Instant>() {
    override fun serialize(instant: Instant, gen: JsonGenerator, sp: SerializerProvider?) {
        gen.writeNumber(instant.toEpochMilli() / 1000)
    }

    override fun handledType(): Class<Instant> = Instant::class.java
}
