package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor

/**
 * Telegram [Gift](https://core.telegram.org/bots/api#gift) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Gift internal constructor(
    val id: String,
    val sticker: Sticker,
    val starCount: Int,
    val upgradeStarCount: Int? = null,
    val totalCount: Int? = null,
    val remainingCount: Int? = null,
    val publisherChat: Chat? = null,
    val personalTotalCount: Int? = null,
    val personalRemainingCount: Int? = null,
    @get:JvmName("getIsPremium")
    val isPremium: Boolean = false,
    val hasColors: Boolean = false,
    val background: Background? = null,
    val uniqueGiftVariantCount: Int? = null
) {
    /**
     * Telegram [GiftBackground](https://core.telegram.org/bots/api#giftbackground) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Background internal constructor(
        val centerColor: RgbColor,
        val edgeColor: RgbColor,
        val textColor: RgbColor
    ) {
        companion object
    }

    companion object
}
