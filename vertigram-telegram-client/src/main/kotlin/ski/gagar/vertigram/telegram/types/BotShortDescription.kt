package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [BotShortDescription](https://core.telegram.org/bots/api#botshortdescription) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BotShortDescription internal constructor(
    val shortDescription: String
) {
    companion object
}
