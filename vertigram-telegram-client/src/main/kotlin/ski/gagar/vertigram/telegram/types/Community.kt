package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [Community](https://core.telegram.org/bots/api#community) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Community internal constructor(
    val id: Long,
    val name: String
) {
    companion object
}
