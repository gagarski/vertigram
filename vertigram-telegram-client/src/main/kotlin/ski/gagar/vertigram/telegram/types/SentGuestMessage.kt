package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [SentGuestMessage](https://core.telegram.org/bots/api#sentguestmessage) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class SentGuestMessage internal constructor(
    val inlineMessageId: String
) {
    companion object
}
