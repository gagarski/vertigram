package ski.gagar.vertigram.telegram.types.colors

import com.fasterxml.jackson.annotation.JsonValue

/**
 * An enum, representing a value for [ski.gagar.vertigram.telegram.types.Chat.Verbose.accentColor].
 *
 * See [Accent Colors](https://core.telegram.org/bots/api#accent-colors) in Telegram docs for up-to-date reference.
 */
enum class AccentColor(
    /**
     * An id of the color, according to the docs
     */
    @JsonValue val id: Int,
    /**
     * List of colors, used in light Telegram theme
     */
    val light: List<RgbColor>,
    /**
     * List of colors, used in light Telegram theme
     */
    val dark: List<RgbColor> = light
) {
    /**
     * This color is unspecified and depends on user theme in the app
     */
    RED(
        0,
        listOf(
            RgbColor(255.toUByte(), 132.toUByte(), 94.toUByte())
        ),
        listOf(
            RgbColor(220.toUByte(), 128.toUByte(), 91.toUByte())
        )
    ),
    /**
     * This color is unspecified and depends on user theme in the app
     */
    ORANGE(
        1,
        listOf(
            RgbColor(
                242.toUByte(), 188.toUByte(), 100.toUByte()
            )
        ),
        listOf(
            RgbColor(
                254.toUByte(), 187.toUByte(), 91.toUByte()
            )
        )
    ),
    /**
     * This color is unspecified and depends on user theme in the app
     */
    PURPLE(
        2,
        listOf(
            RgbColor(
                182.toUByte(), 148.toUByte(), 249.toUByte()
            )
        ),
        listOf(
            RgbColor(
                182.toUByte(), 148.toUByte(), 249.toUByte()
            )
        )
    ),
    /**
     * This color is unspecified and depends on user theme in the app
     */
    GREEN(
        3,
        listOf(
            RgbColor(
                154.toUByte(), 209.toUByte(), 100.toUByte()
            )
        ),
        listOf(
            RgbColor(
                154.toUByte(), 209.toUByte(), 100.toUByte()
            )
        )
    ),
    /**
     * This color is unspecified and depends on user theme in the app
     */
    CYAN(
        4,
        listOf(
            RgbColor(
                91.toUByte(), 203.toUByte(), 227.toUByte()
            )
        ),
        listOf(
            RgbColor(
                91.toUByte(), 203.toUByte(), 227.toUByte()
            )
        )
    ),
    /**
     * This color is unspecified and depends on user theme in the app
     */
    BLUE(
        5,
        listOf(
            RgbColor(
                80.toUByte(), 165.toUByte(), 230.toUByte()
            )
        ),
        listOf(
            RgbColor(
                92.toUByte(), 175.toUByte(), 250.toUByte()
            )
        )
    ),
    /**
     * This color is unspecified and depends on user theme in the app
     */
    PINK(
        6,
        listOf(
            RgbColor(
                255.toUByte(), 138.toUByte(), 172.toUByte()
            )
        ),
        listOf(
            RgbColor(
                232.toUByte(), 116.toUByte(), 154.toUByte()
            )
        )
    ),
    PINK_RED(
        7,
        listOf(
            RgbColor.fromInt(0xE15052),
            RgbColor.fromInt(0xF9AE63)
        ),
        listOf(
            RgbColor.fromInt(0xFF9380),
            RgbColor.fromInt(0x992F37)
        ),
    ),
    YELLOW_ORANGE(
        8,
        listOf(
            RgbColor.fromInt(0xE0802B),
            RgbColor.fromInt(0xFAC534)
        ),
        listOf(
            RgbColor.fromInt(0xECB04E),
            RgbColor.fromInt(0xC35714)
        ),
    ),
    PURPLE_VIOLET(
        9,
        listOf(
            RgbColor.fromInt(0xA05FF3),
            RgbColor.fromInt(0xF48FFF)
        ),
        listOf(
            RgbColor.fromInt(0xC697FF),
            RgbColor.fromInt(0x5E31C8)
        ),
    ),
    LIGHT_GREEN_GREEN(
        10,
        listOf(
            RgbColor.fromInt(0x27A910),
            RgbColor.fromInt(0xA7DC57)
        ),
        listOf(
            RgbColor.fromInt(0xA7EB6E),
            RgbColor.fromInt(0x167E2D)
        ),
    ),
    CYAN_BLUE(
        11,
        listOf(
            RgbColor.fromInt(0x27ACCE),
            RgbColor.fromInt(0x82E8D6)
        ),
        listOf(
            RgbColor.fromInt(0x40D8D0),
            RgbColor.fromInt(0x045C7F)
        ),
    ),
    LIGHT_BLUE_BLUE(
        12,
        listOf(
            RgbColor.fromInt(0x3391D4),
            RgbColor.fromInt(0x7DD3F0)
        ),
        listOf(
            RgbColor.fromInt(0x52BFFF),
            RgbColor.fromInt(0x0B5494)
        ),
    ),
    PINK_DARK_PINK(
        13,
        listOf(
            RgbColor.fromInt(0xDD4371),
            RgbColor.fromInt(0xFFBE9F)
        ),
        listOf(
            RgbColor.fromInt(0xFF86A6),
            RgbColor.fromInt(0x8E366E)
        ),
    ),
    BLUE_RED_WHITE(
        14,
        listOf(
            RgbColor.fromInt(0x247BED),
            RgbColor.fromInt(0xF04856),
            RgbColor.fromInt(0xFFFFFF)
        ),
        listOf(
            RgbColor.fromInt(0x3FA2FE),
            RgbColor.fromInt(0xE5424F),
            RgbColor.fromInt(0xFFFFFF)
        ),
    ),
    ORANGE_GREEN_WHITE(
        15,
        listOf(
            RgbColor.fromInt(0xD67722),
            RgbColor.fromInt(0x1EA011),
            RgbColor.fromInt(0xFFFFFF)
        ),
        listOf(
            RgbColor.fromInt(0xFF905E),
            RgbColor.fromInt(0x32A527),
            RgbColor.fromInt(0xFFFFFF)
        ),
    ),
    GREEN_RED_WHITE(
        16,
        listOf(
            RgbColor.fromInt(0x179E42),
            RgbColor.fromInt(0xE84A3F),
            RgbColor.fromInt(0xFFFFFF)
        ),
        listOf(
            RgbColor.fromInt(0x66D364),
            RgbColor.fromInt(0xD5444F),
            RgbColor.fromInt(0xFFFFFF)
        ),
    ),
    BLUE_GREEN_WHITE(
        17,
        listOf(
            RgbColor.fromInt(0x2894AF),
            RgbColor.fromInt(0x6FC456),
            RgbColor.fromInt(0xFFFFFF)
        ),
        listOf(
            RgbColor.fromInt(0x22BCE2),
            RgbColor.fromInt(0x3DA240),
            RgbColor.fromInt(0xFFFFFF)
        ),
    ),
    BLUE_RED_YELLOW(
        18,
        listOf(
            RgbColor.fromInt(0x0C9AB3),
            RgbColor.fromInt(0xFFAD95),
            RgbColor.fromInt(0xFFE6B5)
        ),
        listOf(
            RgbColor.fromInt(0x22BCE2),
            RgbColor.fromInt(0xFF9778),
            RgbColor.fromInt(0xFFDA6B)
        ),
    ),
    VIOLET_ORANGE_YELLOW(
        19,
        listOf(
            RgbColor.fromInt(0x7757D6),
            RgbColor.fromInt(0xF79610),
            RgbColor.fromInt(0xFFDE8E)
        ),
        listOf(
            RgbColor.fromInt(0x9791FF),
            RgbColor.fromInt(0xF2731D),
            RgbColor.fromInt(0xFFDB59)
        ),
    ),
    BLUE_YELLOW_WHITE(
        20,
        listOf(
            RgbColor.fromInt(0x1585CF),
            RgbColor.fromInt(0xF2AB1D),
            RgbColor.fromInt(0xFFFFFF)
        ),
        listOf(
            RgbColor.fromInt(0x3DA6EB),
            RgbColor.fromInt(0xEEA51D),
            RgbColor.fromInt(0xFFFFFF)
        ),
    );

    companion object {
        val byId = entries.associateBy { it.id }
    }
}