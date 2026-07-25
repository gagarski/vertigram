package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * This object represents the bot's name.
 *
 * See Telegram's [BotName](https://core.telegram.org/bots/api#botname) documentation.
 */
@TelegramCodegen.Type
data class BotName internal constructor(
    /** The bot's name. */
    val name: String
) {
    companion object
}
