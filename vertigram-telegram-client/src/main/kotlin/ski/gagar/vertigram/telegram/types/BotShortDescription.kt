package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * This object represents the bot's short description.
 *
 * See Telegram's [BotShortDescription](https://core.telegram.org/bots/api#botshortdescription) documentation.
 */
@TelegramCodegen.Type
data class BotShortDescription internal constructor(
    /** The bot's short description. */
    val shortDescription: String
) {
    companion object
}
