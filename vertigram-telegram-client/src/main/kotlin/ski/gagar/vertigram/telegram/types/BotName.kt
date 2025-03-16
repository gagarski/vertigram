package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [BotName](https://core.telegram.org/bots/api#botname) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BotName internal constructor(
    val name: String
) {
    companion object
}
