package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

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
    val upgradeStarCount: Int,
    val totalCount: Int,
    val remainingCount: Int
) {
    companion object
}
