package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonValue

enum class TopicColor(val color: RgbColor) {
    CYAN(RgbColor(0x6F.toUByte(), 0xB9.toUByte(), 0xF0.toUByte())),
    YELLOW(RgbColor(0xFF.toUByte(), 0xD6.toUByte(), 0x7E.toUByte())),
    PURPLE(RgbColor(0xC8.toUByte(), 0x86.toUByte(), 0xDB.toUByte())),
    GREEN(RgbColor(0x8E.toUByte(), 0xEE.toUByte(), 0x98.toUByte())),
    PINK(RgbColor(0xFF.toUByte(), 0x93.toUByte(), 0xB2.toUByte())),
    RED(RgbColor(0xFB.toUByte(), 0x6F.toUByte(), 0x5F.toUByte()));

    @JsonValue
    fun toValue() = color.toInt()
}
