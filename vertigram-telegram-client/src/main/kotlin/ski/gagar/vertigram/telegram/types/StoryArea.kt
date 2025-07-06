package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.ArgbColor

/**
 * Telegram [StoryArea](https://core.telegram.org/bots/api#storyarea) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class StoryArea internal constructor(
    val position: Position,
    val type: Type
) {
    /**
     * Telegram [StoryAreaPosition](https://core.telegram.org/bots/api#storyareaposition) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Position internal constructor(
        val xPercentage: Double,
        val yPercentage: Double,
        val widthPercentage: Double,
        val heightPercentage: Double,
        val rotationAngle: Double,
        val cornerRadiusPercentage: Double
    ) {
        companion object
    }

    /**
     * Telegram [StoryAreaType](https://core.telegram.org/bots/api#storyareatype) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
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
         * Telegram [StoryAreaTypeLocation](https://core.telegram.org/bots/api#storyareatypelocation) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Location internal constructor(
            val latitude: Double,
            val longitude: Double,
            val address: Address
        ): Type {
            override val type: Kind = Kind.LOCATION

            /**
             * Telegram [LocationAddress](https://core.telegram.org/bots/api#locationaddress) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Address internal constructor(
                val countryCode: String,
                val state: String? = null,
                val city: String? = null,
                val street: String? = null,
            ) {
                companion object
            }

            companion object
        }

        /**
         * Telegram [StoryAreaTypeSuggestedReaction](https://core.telegram.org/bots/api#storyareatypesuggestedreaction) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class SuggestedReaction internal constructor(
            val reactionType: Reaction,
            @get:JvmName("getIsDark")
            val isDark: Boolean = false,
            @get:JvmName("getIsFlipped")
            val isFlipped: Boolean = false
        ) : Type {
            override val type: Kind = Kind.SUGGESTED_REACTION
            companion object
        }

        /**
         * Telegram [StoryAreaTypeLink](https://core.telegram.org/bots/api#storyareatypelink) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Link internal constructor(
            val url: String
        ) : Type {
            override val type: Kind = Kind.LINK
            companion object
        }

        /**
         * Telegram [StoryAreaTypeWeather](https://core.telegram.org/bots/api#storyareatypeweather) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Weather internal constructor(
            val temperature: Double,
            val emoji: String,
            val backgroundColor: ArgbColor
        ) : Type {
            override val type: Kind = Kind.WEATHER
            companion object
        }

        /**
         * Telegram [StoryAreaTypeUniqueGift](https://core.telegram.org/bots/api#storyareatypeuniquegift) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class UniqueGift internal constructor(
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
