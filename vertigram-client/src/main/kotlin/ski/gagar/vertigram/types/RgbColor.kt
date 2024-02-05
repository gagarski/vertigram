package ski.gagar.vertigram.types

data class RgbColor(val r: UByte, val g: UByte, val b: UByte) {
    fun toInt(): Int {
        var buf = 0
        buf = buf or r.toInt()
        buf = buf shl 8
        buf = buf or g.toInt()
        buf = buf shl 8
        buf = buf or b.toInt()
        return buf
    }

    companion object {
        fun fromInt(int: Int): RgbColor {
            val r = (int shr 16).toUByte()
            val g = (int shr 8).toUByte()
            val b = int.toUByte()
            return RgbColor(r, g, b)
        }
    }
}
