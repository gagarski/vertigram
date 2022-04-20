package ski.gagar.vxutil.vertigram.util.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant

internal class UnixTimestampDeserializer : JsonDeserializer<Instant>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Instant {
        return Instant.ofEpochSecond(parser.longValue)
    }

    override fun handledType(): Class<Instant> = Instant::class.java
}
