package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import ski.gagar.vertigram.types.RgbColor

internal class RgbColorSerializer : JsonSerializer<RgbColor>() {
    override fun serialize(color: RgbColor, gen: JsonGenerator, sp: SerializerProvider?) {
        gen.writeNumber(color.toInt())
    }

    override fun handledType(): Class<RgbColor> = RgbColor::class.java
}
