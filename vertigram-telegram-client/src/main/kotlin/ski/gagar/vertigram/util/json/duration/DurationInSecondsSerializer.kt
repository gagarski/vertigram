package ski.gagar.vertigram.util.json.duration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.Duration

/**
 * Serializes [Duration] in seconds, used in [ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER].
 */
internal class DurationInSecondsSerializer : JsonSerializer<Duration>() {
    override fun serialize(duration: Duration, gen: JsonGenerator, sp: SerializerProvider) {
        gen.writeNumber(duration.toSeconds())
    }

    override fun handledType(): Class<Duration> = Duration::class.java
}
