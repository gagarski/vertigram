package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor

/**
 * Telegram [UniqueGift](https://core.telegram.org/bots/api#uniquegift) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class UniqueGift internal constructor(
    val baseName: String,
    val name: String,
    val number: Int,
    val model: Model,
    val symbol: Symbol,
    val backdrop: Backdrop
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
        val rarityPerMille: Int
    ) {
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
    companion object
}
