package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Represents an HTTP link.
 *
 * See Telegram's [Link](https://core.telegram.org/bots/api#link) documentation.
 */
@TelegramCodegen.Type
data class Link internal constructor(
    /** URL of the link. */
    val url: String
) {
    companion object
}
