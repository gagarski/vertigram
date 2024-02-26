package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Duration

/**
 * Deserializes [Duration] in seconds, used in [TELEGRAM_JSON_MAPPER].
 */
internal class DurationInSecondsDeserializer : JsonDeserializer<Duration>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Duration {
        return Duration.ofSeconds(parser.longValue)
    }

    override fun handledType(): Class<Duration> = Duration::class.java
}
