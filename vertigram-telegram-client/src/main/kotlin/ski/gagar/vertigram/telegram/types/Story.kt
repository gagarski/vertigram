package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.ArgbColor
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents a story.
 *
 * See Telegram's [Story](https://core.telegram.org/bots/api#story) documentation.
 */
@TelegramCodegen.Type
data class Story internal constructor(
    /** Chat that posted the story. */
    val chat: Chat,
    /** Unique identifier for the story in the chat. */
    val id: Long
) {
    /**
     * Describes a clickable area on a story media.
     *
     * See Telegram's [StoryArea](https://core.telegram.org/bots/api#storyarea) documentation.
     */
    @TelegramCodegen.Type
    data class Area internal constructor(
        /** Position of the area. */
        val position: Position,
        /** Type of the area. */
        val type: Type
    ) {
        /**
         * Describes the position of a clickable area within a story.
         *
         * See Telegram's [StoryAreaPosition](https://core.telegram.org/bots/api#storyareaposition) documentation.
         */
        @TelegramCodegen.Type
        data class Position internal constructor(
            /** Horizontal center coordinate as a percentage of the media width. */
            val xPercentage: Double,
            /** Vertical center coordinate as a percentage of the media height. */
            val yPercentage: Double,
            /** Width of the area as a percentage of the media width. */
            val widthPercentage: Double,
            /** Height of the area as a percentage of the media height. */
            val heightPercentage: Double,
            /** Clockwise rotation angle of the rectangle in degrees. */
            val rotationAngle: Double,
            /** Corner radius as a percentage of the media width. */
            val cornerRadiusPercentage: Double
        ) {
            companion object
        }

        /**
         * Describes the type of a clickable area on a story.
         *
         * See Telegram's [StoryAreaType](https://core.telegram.org/bots/api#storyareatype) documentation.
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
        @JsonSubTypes(
            JsonSubTypes.Type(value = Type.Location::class, name = Type.Kind.LOCATION_STR),
            JsonSubTypes.Type(value = Type.SuggestedReaction::class, name = Type.Kind.SUGGESTED_REACTION_STR),
            JsonSubTypes.Type(value = Type.Link::class, name = Type.Kind.LINK_STR),
            JsonSubTypes.Type(value = Type.Weather::class, name = Type.Kind.WEATHER_STR),
            JsonSubTypes.Type(value = Type.UniqueGift::class, name = Type.Kind.UNIQUE_GIFT_STR),
        )
        sealed interface Type {
            val type: Kind

            /**
             * Case when the story area points to a location.
             *
             * See Telegram's [StoryAreaTypeLocation](https://core.telegram.org/bots/api#storyareatypelocation)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Location internal constructor(
                /** Location latitude in degrees. */
                val latitude: Double,
                /** Location longitude in degrees. */
                val longitude: Double,
                /** Address of the location. */
                val address: Address
            ): Type {
                override val type: Kind = Kind.LOCATION

                /**
                 * Describes the physical address of a location.
                 *
                 * See Telegram's [LocationAddress](https://core.telegram.org/bots/api#locationaddress) documentation.
                 */
                @TelegramCodegen.Type
                data class Address internal constructor(
                    /** Two-letter ISO 3166-1 alpha-2 country code. */
                    val countryCode: String,
                    /** State, if applicable. */
                    val state: String? = null,
                    /** City, if applicable. */
                    val city: String? = null,
                    /** Street, if applicable. */
                    val street: String? = null,
                ) {
                    companion object
                }

                companion object
            }

            /**
             * Case when the story area suggests a reaction.
             *
             * See Telegram's
             * [StoryAreaTypeSuggestedReaction](https://core.telegram.org/bots/api#storyareatypesuggestedreaction)
             * documentation.
             */
            @TelegramCodegen.Type
            data class SuggestedReaction internal constructor(
                /** Type of the reaction. */
                val reactionType: Reaction,
                /** Whether the reaction area has a dark background. */
                @get:JvmName("getIsDark")
                val isDark: Boolean = false,
                /** Whether the reaction area corner is flipped. */
                @get:JvmName("getIsFlipped")
                val isFlipped: Boolean = false
            ) : Type {
                override val type: Kind = Kind.SUGGESTED_REACTION
                companion object
            }

            /**
             * Case when the story area points to an HTTP or HTTPS URL.
             *
             * See Telegram's [StoryAreaTypeLink](https://core.telegram.org/bots/api#storyareatypelink) documentation.
             */
            @TelegramCodegen.Type
            data class Link internal constructor(
                /** HTTP or HTTPS URL to be opened. */
                val url: String
            ) : Type {
                override val type: Kind = Kind.LINK
                companion object
            }

            /**
             * Case when the story area contains weather information.
             *
             * See Telegram's [StoryAreaTypeWeather](https://core.telegram.org/bots/api#storyareatypeweather)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Weather internal constructor(
                /** Temperature in degrees Celsius. */
                val temperature: Double,
                /** Emoji representing the weather. */
                val emoji: String,
                /** Background color of the area. */
                val backgroundColor: ArgbColor
            ) : Type {
                override val type: Kind = Kind.WEATHER
                companion object
            }

            /**
             * Case when the story area points to a unique gift.
             *
             * See Telegram's [StoryAreaTypeUniqueGift](https://core.telegram.org/bots/api#storyareatypeuniquegift)
             * documentation.
             */
            @TelegramCodegen.Type
            data class UniqueGift internal constructor(
                /** Name of the unique gift. */
                val name: String
            ) : Type {
                override val type: Kind = Kind.UNIQUE_GIFT
                companion object
            }

            /**
             * A value for [Type.type] field.
             */
            enum class Kind {
                @JsonProperty(LOCATION_STR)
                LOCATION,
                @JsonProperty(SUGGESTED_REACTION_STR)
                SUGGESTED_REACTION,
                @JsonProperty(LINK_STR)
                LINK,
                @JsonProperty(WEATHER_STR)
                WEATHER,
                @JsonProperty(UNIQUE_GIFT_STR)
                UNIQUE_GIFT;

                companion object {
                    const val LOCATION_STR = "location"
                    const val SUGGESTED_REACTION_STR = "suggested_reaction"
                    const val LINK_STR = "link"
                    const val WEATHER_STR = "weather"
                    const val UNIQUE_GIFT_STR = "unique_gift"
                }
            }

            companion object
        }

        companion object
    }

    companion object
}
