package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Describes an inline message sent by a guest bot.
 *
 * See Telegram's [SentGuestMessage](https://core.telegram.org/bots/api#sentguestmessage) documentation.
 */
@TelegramCodegen.Type
data class SentGuestMessage internal constructor(
    /** Identifier of the sent inline message. */
    val inlineMessageId: String
) {
    companion object
}
