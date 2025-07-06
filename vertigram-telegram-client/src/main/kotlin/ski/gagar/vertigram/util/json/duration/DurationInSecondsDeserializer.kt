package ski.gagar.vertigram.util.json.duration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Duration

/**
 * Deserializes [Duration] in seconds, used in [ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER].
 */
internal class DurationInSecondsDeserializer : JsonDeserializer<Duration>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Duration {
        return Duration.ofSeconds(parser.longValue)
    }

    override fun handledType(): Class<Duration> = Duration::class.java
}
