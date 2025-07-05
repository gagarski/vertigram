package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [BotDescription](https://core.telegram.org/bots/api#botdescription) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BotDescription internal constructor(
    val description: String
) {
    companion object
}
