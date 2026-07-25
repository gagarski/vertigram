package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor

/**
 * This object represents a chat background.
 *
 * See Telegram's [ChatBackground](https://core.telegram.org/bots/api#chatbackground) documentation.
 */
@TelegramCodegen.Type
data class ChatBackground internal constructor(
    /** Type of the background. */
    val type: Type
) {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Type.Fill::class, name = Kind.FILL_STR),
        JsonSubTypes.Type(value = Type.Wallpaper::class, name = Kind.WALLPAPER_STR),
        JsonSubTypes.Type(value = Type.Pattern::class, name = Kind.PATTERN_STR),
        JsonSubTypes.Type(value = Type.ChatTheme::class, name = Kind.CHAT_THEME_STR),
    )
    /**
     * This object describes the type of a background.
     *
     * See Telegram's [BackgroundType](https://core.telegram.org/bots/api#backgroundtype) documentation.
     */
    sealed interface Type {
        val type: Kind

        /**
         * The background is automatically filled based on the selected colors.
         *
         * See Telegram's [BackgroundTypeFill](https://core.telegram.org/bots/api#backgroundtypefill) documentation.
         */
        @TelegramCodegen.Type
        data class Fill internal constructor(
            /** The background fill. */
            val fill: Value,
            /** Dimming of the background in dark themes, as a percentage; 0-100. */
            val darkThemeDimming: Int
        ) : Type {
            override val type: Kind = Kind.FILL

            @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
            @JsonSubTypes(
                JsonSubTypes.Type(value = Value.Solid::class, name = Type.SOLID_STR),
                JsonSubTypes.Type(value = Value.Gradient::class, name = Type.GRADIENT_STR),
                JsonSubTypes.Type(value = Value.FreeformGradient::class, name = Type.FREEFORM_GRADIENT_STR),
            )
            /**
             * This object describes the way a background is filled based on the selected colors.
             *
             * See Telegram's [BackgroundFill](https://core.telegram.org/bots/api#backgroundfill) documentation.
             */
            sealed interface Value {
                val type: Type

                /**
                 * The background is filled using the selected color.
                 *
                 * See Telegram's [BackgroundFillSolid](https://core.telegram.org/bots/api#backgroundfillsolid)
                 * documentation.
                 */
                @TelegramCodegen.Type
                data class Solid internal constructor(
                    /** The color of the background fill in the RGB24 format. */
                    val color: RgbColor
                ) : Value {
                    override val type: Type = Type.SOLID
                    companion object
                }

                /**
                 * The background is a gradient fill.
                 *
                 * See Telegram's [BackgroundFillGradient](https://core.telegram.org/bots/api#backgroundfillgradient)
                 * documentation.
                 */
                @TelegramCodegen.Type
                data class Gradient internal constructor(
                    /** Top color of the gradient in the RGB24 format. */
                    val topColor: RgbColor,
                    /** Bottom color of the gradient in the RGB24 format. */
                    val bottomColor: RgbColor,
                    /** Clockwise rotation angle of the background fill in degrees; 0-359. */
                    val rotationAngle: Int
                ) : Value {
                    override val type: Type = Type.GRADIENT
                    companion object
                }

                /**
                 * The background is a freeform gradient that rotates after every message in the chat.
                 *
                 * See Telegram's
                 * [BackgroundFillFreeformGradient](https://core.telegram.org/bots/api#backgroundfillfreeformgradient)
                 * documentation.
                 */
                @TelegramCodegen.Type
                data class FreeformGradient internal constructor(
                    /** The 3 or 4 base colors used to generate the freeform gradient in the RGB24 format. */
                    val colors: List<RgbColor>
                ) : Value {
                    override val type: Type = Type.FREEFORM_GRADIENT
                    companion object
                }
            }

            /**
             * A value for [type]
             */
            enum class Type {
                @JsonProperty(SOLID_STR)
                SOLID,
                @JsonProperty(GRADIENT_STR)
                GRADIENT,
                @JsonProperty(FREEFORM_GRADIENT_STR)
                FREEFORM_GRADIENT;

                companion object {
                    const val SOLID_STR = "solid"
                    const val GRADIENT_STR = "gradient"
                    const val FREEFORM_GRADIENT_STR = "freeform"
                }
            }

            companion object
        }

        /**
         * The background is a wallpaper in the JPEG format.
         *
         * See Telegram's [BackgroundTypeWallpaper](https://core.telegram.org/bots/api#backgroundtypewallpaper)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Wallpaper internal constructor(
            /** Document with the wallpaper. */
            val document: Document,
            /** Dimming of the background in dark themes, as a percentage; 0-100. */
            val darkThemeDimming: Int,
            /** `true` if the wallpaper is downscaled to fit in a 450x450 square and box-blurred with radius 12. */
            @get:JvmName("getIsBlurred")
            val isBlurred: Boolean = false,
            /** `true` if the background moves slightly when the device is tilted. */
            @get:JvmName("getIsMoving")
            val isMoving: Boolean = false,
        ) : Type {
            override val type: Kind = Kind.WALLPAPER
            companion object
        }

        /**
         * The background is a .PNG or .TGV pattern to be combined with the background fill chosen by the user.
         *
         * See Telegram's [BackgroundTypePattern](https://core.telegram.org/bots/api#backgroundtypepattern)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Pattern internal constructor(
            /** Document with the pattern. */
            val document: Document,
            /** The background fill that is combined with the pattern. */
            val fill: Fill.Value,
            /** Intensity of the pattern when it is shown above the filled background; 0-100. */
            val intensity: Int,
            /**
             * `true` if the background fill must be applied only to the pattern itself. All other pixels are black in
             * this case. For dark themes only.
             */
            @get:JvmName("getIsInverted")
            val isInverted: Boolean = false,
            /** `true` if the background moves slightly when the device is tilted. */
            @get:JvmName("getIsMoving")
            val isMoving: Boolean = false,
        ) : Type {
            override val type: Kind = Kind.PATTERN
            companion object
        }

        /**
         * The background is taken directly from a built-in chat theme.
         *
         * See Telegram's [BackgroundTypeChatTheme](https://core.telegram.org/bots/api#backgroundtypechattheme)
         * documentation.
         */
        @TelegramCodegen.Type
        data class ChatTheme internal constructor(
            /** Name of the chat theme, which is usually an emoji. */
            val themeName: String
        ) : Type {
            override val type: Kind = Kind.CHAT_THEME
            companion object
        }
    }



    /**
     * A value for [Type.type]
     */
    enum class Kind {
        @JsonProperty(FILL_STR)
        FILL,
        @JsonProperty(WALLPAPER_STR)
        WALLPAPER,
        @JsonProperty(PATTERN_STR)
        PATTERN,
        @JsonProperty(CHAT_THEME_STR)
        CHAT_THEME;

        companion object {
            const val FILL_STR = "fill"
            const val WALLPAPER_STR = "wallpaper"
            const val PATTERN_STR = "pattern"
            const val CHAT_THEME_STR = "chat_theme"
        }
    }

    companion object
}
