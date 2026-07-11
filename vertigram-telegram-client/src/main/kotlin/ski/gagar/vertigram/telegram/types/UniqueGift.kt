package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor

/**
 * Telegram [UniqueGift](https://core.telegram.org/bots/api#uniquegift) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class UniqueGift internal constructor(
    val giftId: String? = null,
    val baseName: String,
    val name: String,
    val number: Int,
    val model: Model,
    val symbol: Symbol,
    val backdrop: Backdrop,
    val publisherChat: Chat? = null,
    @get:JvmName("getIsPremium")
    val isPremium: Boolean = false,
    @get:JvmName("getIsFromBlockchain")
    val isFromBlockchain: Boolean = false,
    @get:JvmName("getIsBurned")
    val isBurned: Boolean = false,
    val colors: Colors? = null
) {
    /**
     * Telegram [UniqueGiftModel](https://core.telegram.org/bots/api#uniquegiftmodel) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Model internal constructor(
        val name: String,
        val sticker: Sticker,
        val rarityPerMille: Int,
        val rarity: Rarity? = null
    ) {
        enum class Rarity {
            @JsonProperty(COMMON_STR)
            COMMON,
            @JsonProperty(RARE_STR)
            RARE,
            @JsonProperty(EPIC_STR)
            EPIC,
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
     * Telegram [UniqueGiftSymbol](https://core.telegram.org/bots/api#uniquegiftsymbol) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Symbol internal constructor(
        val name: String,
        val sticker: Sticker,
        val rarityPerMille: Int
    ) {
        companion object
    }

    /**
     * Telegram [UniqueGiftBackdrop](https://core.telegram.org/bots/api#uniquegiftbackdrop) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Backdrop internal constructor(
        val name: String,
        val colors: Colors,
        val rarityPerMille: Int
    ) {
        @TelegramCodegen.Type
        data class Colors internal constructor(
            val centerColor: RgbColor,
            val edgeColor: RgbColor,
            val symbolColor: RgbColor,
            val textColor: RgbColor
        ) {
            companion object
        }
        companion object
    }

    /**
     * Telegram [UniqueGiftColors](https://core.telegram.org/bots/api#uniquegiftcolors) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Colors internal constructor(
        val modelCustomEmojiId: String,
        val symbolCustomEmojiId: String,
        val lightThemeMainColor: RgbColor,
        val lightThemeOtherColors: List<RgbColor>,
        val darkThemeMainColor: RgbColor,
        val darkThemeOtherColors: List<RgbColor>
    ) {
        companion object
    }

    companion object
}
