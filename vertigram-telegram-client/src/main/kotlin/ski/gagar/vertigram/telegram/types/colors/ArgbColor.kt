package ski.gagar.vertigram.telegram.types.colors

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import ski.gagar.vertigram.telegram.types.colors.ArgbColor.Companion.fromInt

/**
 * ARGB-color
 */
@JsonSerialize(using = ArgbColorSerializer::class)
@JsonDeserialize(using = ArgbColorDeserializer::class)
data class ArgbColor(
    /**
     * Alpha component
     */
    val alpha: UByte,
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
     * Leftmost byte is [alpha], second is [red], third is [green], fourth is [blue]
     *
     * @see fromInt
     */
    fun toInt(): Int {
        var buf = 0
        buf = buf or alpha.toInt()
        buf = buf shl 8
        buf = buf or red.toInt()
        buf = buf shl 8
        buf = buf or green.toInt()
        buf = buf shl 8
        buf = buf or blue.toInt()
        return buf
    }

    companion object {
        /**
         * Create an [ArgbColor] from [Int] representation, same as returned by [toInt]
         *
         * @see toInt
         */
        fun fromInt(int: Int): ArgbColor {
            val a = (int shr 24).toUByte()
            val r = (int shr 16).toUByte()
            val g = (int shr 8).toUByte()
            val b = int.toUByte()
            return ArgbColor(a,r, g, b)
        }
    }
}

/**
 * Serializer for [RgbColor] which serializes it as a number.
 */
internal class ArgbColorDeserializer : JsonDeserializer<ArgbColor>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): ArgbColor {
        return ArgbColor.fromInt(parser.intValue)
    }

    override fun handledType(): Class<ArgbColor> = ArgbColor::class.java
}

/**
 * Deserializer for [RgbColor] which deserializes it from a JSON number
 */
internal class ArgbColorSerializer : JsonSerializer<ArgbColor>() {
    override fun serialize(color: ArgbColor, gen: JsonGenerator, sp: SerializerProvider?) {
        gen.writeNumber(color.toInt())
    }

    override fun handledType(): Class<ArgbColor> = ArgbColor::class.java
}
