package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor

/**
 * Describes a unique gift that was upgraded from a regular gift.
 *
 * See Telegram's [UniqueGift](https://core.telegram.org/bots/api#uniquegift) documentation.
 */
@TelegramCodegen.Type
data class UniqueGift internal constructor(
    /** Unique identifier of the gift. */
    val giftId: String? = null,
    /** Human-readable name of the regular gift from which this unique gift was upgraded. */
    val baseName: String,
    /** Human-readable name of the unique gift. */
    val name: String,
    /** Unique number of the upgraded gift among gifts upgraded from the same regular gift. */
    val number: Int,
    /** Model of the gift. */
    val model: Model,
    /** Symbol of the gift. */
    val symbol: Symbol,
    /** Backdrop of the gift. */
    val backdrop: Backdrop,
    /** Chat that published the gift. */
    val publisherChat: Chat? = null,
    /** Whether the gift is a premium gift. */
    @get:JvmName("getIsPremium")
    val isPremium: Boolean = false,
    /** Whether the gift was assigned from a blockchain. */
    @get:JvmName("getIsFromBlockchain")
    val isFromBlockchain: Boolean = false,
    /** Whether the gift was burned. */
    @get:JvmName("getIsBurned")
    val isBurned: Boolean = false,
    /** Colors used by the gift. */
    val colors: Colors? = null
) {
    /**
     * Describes the model of a unique gift.
     *
     * See Telegram's [UniqueGiftModel](https://core.telegram.org/bots/api#uniquegiftmodel) documentation.
     */
    @TelegramCodegen.Type
    data class Model internal constructor(
        /** Name of the model. */
        val name: String,
        /** Sticker representing the unique gift. */
        val sticker: Sticker,
        /** Number of unique gifts that receive this model for every 1000 gifts upgraded. */
        val rarityPerMille: Int,
        /** Rarity of the model. */
        val rarity: Rarity? = null
    ) {
        enum class Rarity {
            /** Common model. */
            @JsonProperty(COMMON_STR)
            COMMON,
            /** Rare model. */
            @JsonProperty(RARE_STR)
            RARE,
            /** Epic model. */
            @JsonProperty(EPIC_STR)
            EPIC,
            /** Legendary model. */
            @JsonProperty(LEGENDARY_STR)
            LEGENDARY;

            companion object {
                const val COMMON_STR = "common"
                const val RARE_STR = "rare"
                const val EPIC_STR = "epic"
                const val LEGENDARY_STR = "legendary"
            }
        }

        companion object
    }

    /**
     * Describes the symbol shown on the pattern of a unique gift.
     *
     * See Telegram's [UniqueGiftSymbol](https://core.telegram.org/bots/api#uniquegiftsymbol) documentation.
     */
    @TelegramCodegen.Type
    data class Symbol internal constructor(
        /** Name of the symbol. */
        val name: String,
        /** Sticker representing the symbol. */
        val sticker: Sticker,
        /** Number of unique gifts that receive this symbol for every 1000 gifts upgraded. */
        val rarityPerMille: Int
    ) {
        companion object
    }

    /**
     * Describes the backdrop of a unique gift.
     *
     * See Telegram's [UniqueGiftBackdrop](https://core.telegram.org/bots/api#uniquegiftbackdrop) documentation.
     */
    @TelegramCodegen.Type
    data class Backdrop internal constructor(
        /** Name of the backdrop. */
        val name: String,
        /** Colors of the backdrop. */
        val colors: Colors,
        /** Number of unique gifts that receive this backdrop for every 1000 gifts upgraded. */
        val rarityPerMille: Int
    ) {
        /** Describes the colors of a unique gift backdrop. */
        @TelegramCodegen.Type
        data class Colors internal constructor(
            /** Center background color. */
            val centerColor: RgbColor,
            /** Edge background color. */
            val edgeColor: RgbColor,
            /** Color used for the symbol. */
            val symbolColor: RgbColor,
            /** Color used for the text. */
            val textColor: RgbColor
        ) {
            companion object
        }
        companion object
    }

    /**
     * Describes the colors of a unique gift.
     *
     * See Telegram's [UniqueGiftColors](https://core.telegram.org/bots/api#uniquegiftcolors) documentation.
     */
    @TelegramCodegen.Type
    data class Colors internal constructor(
        /** Custom emoji identifier used for the model. */
        val modelCustomEmojiId: String,
        /** Custom emoji identifier used for the symbol. */
        val symbolCustomEmojiId: String,
        /** Main color used in the light theme. */
        val lightThemeMainColor: RgbColor,
        /** Other colors used in the light theme. */
        val lightThemeOtherColors: List<RgbColor>,
        /** Main color used in the dark theme. */
        val darkThemeMainColor: RgbColor,
        /** Other colors used in the dark theme. */
        val darkThemeOtherColors: List<RgbColor>
    ) {
        companion object
    }

    companion object
}
