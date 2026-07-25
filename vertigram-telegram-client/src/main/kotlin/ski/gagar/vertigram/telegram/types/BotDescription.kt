package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * This object represents the bot's description.
 *
 * See Telegram's [BotDescription](https://core.telegram.org/bots/api#botdescription) documentation.
 */
@TelegramCodegen.Type
data class BotDescription internal constructor(
    /** The bot's description. */
    val description: String
) {
    companion object
}
