package ski.gagar.vertigram.util.json.duration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Duration

/**
 * Deserializes [Duration] in seconds (supporting fractional part), used in [ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER].
 */
internal class DurationInSecondsFractionalDeserializer : JsonDeserializer<Duration>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Duration {
        val double = parser.doubleValue
        val seconds = double.toLong()
        val millis = ((double % 1.0) * 1000).toLong()
        return Duration.ofSeconds(seconds) + Duration.ofMillis(millis)
    }

    override fun handledType(): Class<Duration> = Duration::class.java
}
