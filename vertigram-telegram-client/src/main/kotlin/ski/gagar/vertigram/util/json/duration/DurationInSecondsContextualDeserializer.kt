package ski.gagar.vertigram.util.json.duration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import ski.gagar.vertigram.util.json.annotations.Fractional
import java.time.Duration

/**
 * Serializes [Duration] in seconds, used in [ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER].
 */
internal class DurationInSecondsContextualDeserializer : JsonDeserializer<Duration>(), ContextualDeserializer {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Duration {
        error("Should not be called")
    }

    override fun createContextual(
        ctxt: DeserializationContext,
        property: BeanProperty
    ): JsonDeserializer<Duration>? {
        val fractional = property.getAnnotation(Fractional::class.java) != null
        return if (fractional) {
            DurationInSecondsFractionalDeserializer()
        } else {
            DurationInSecondsDeserializer()
        }
    }

    override fun handledType(): Class<Duration> = Duration::class.java
}
