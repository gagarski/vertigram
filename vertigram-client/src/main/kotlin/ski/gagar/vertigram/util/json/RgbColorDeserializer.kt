package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import ski.gagar.vertigram.types.RgbColor

internal class RgbColorDeserializer : JsonDeserializer<RgbColor>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): RgbColor {
        return RgbColor.fromInt(parser.intValue)
    }

    override fun handledType(): Class<RgbColor> = RgbColor::class.java
}
