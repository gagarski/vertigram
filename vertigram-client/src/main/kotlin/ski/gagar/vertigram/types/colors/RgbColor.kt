package ski.gagar.vertigram.types.colors

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * Plain RGB-color
 */
@JsonSerialize(using = RgbColorSerializer::class)
@JsonDeserialize(using = RgbColorDeserializer::class)
data class RgbColor(
    /**
     * Red component
     */
    val red: UByte,
    /**
     * Green component
     * */
    val green: UByte,
    /**
     * Blue component
     */
    val blue: UByte
) {
    /**
     * Convert to integer representation.
     *
     * Leftmost byte is always 0, second is [red], third is [green], fourth is [blue]
     *
     * @see fromInt
     */
    fun toInt(): Int {
        var buf = 0
        buf = buf or red.toInt()
        buf = buf shl 8
        buf = buf or green.toInt()
        buf = buf shl 8
        buf = buf or blue.toInt()
        return buf
    }

    companion object {
        /**
         * Create an [RgbColor] from [Int] representation, same as returned by [toInt]
         *
         * @see toInt
         */
        fun fromInt(int: Int): RgbColor {
            val r = (int shr 16).toUByte()
            val g = (int shr 8).toUByte()
            val b = int.toUByte()
            return RgbColor(r, g, b)
        }
    }
}

/**
 * Serializer for [RgbColor] which serializes it as a number.
 */
internal class RgbColorDeserializer : JsonDeserializer<RgbColor>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): RgbColor {
        return RgbColor.fromInt(parser.intValue)
    }

    override fun handledType(): Class<RgbColor> = RgbColor::class.java
}

/**
 * Deserializer for [RgbColor] which deserializes it from a JSON number
 */
internal class RgbColorSerializer : JsonSerializer<RgbColor>() {
    override fun serialize(color: RgbColor, gen: JsonGenerator, sp: SerializerProvider?) {
        gen.writeNumber(color.toInt())
    }

    override fun handledType(): Class<RgbColor> = RgbColor::class.java
}
