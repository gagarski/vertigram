package ski.gagar.vertigram.util.json.duration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import ski.gagar.vertigram.util.json.annotations.Fractional
import java.time.Duration

class DurationInSecondsContextualSerializer : ContextualSerializer, JsonSerializer<Duration>() {
    override fun createContextual(
        prov: SerializerProvider,
        property: BeanProperty
    ): JsonSerializer<Duration> {
        val fractional = property.getAnnotation(Fractional::class.java) != null
        return if (fractional) {
            DurationInSecondsFractionalSerializer()
        } else {
            DurationInSecondsSerializer()
        }
    }

    override fun serialize(
        value: Duration?,
        gen: JsonGenerator?,
        serializers: SerializerProvider?
    ) {
        error("Should not be called")
    }

    override fun handledType(): Class<Duration> = Duration::class.java
}