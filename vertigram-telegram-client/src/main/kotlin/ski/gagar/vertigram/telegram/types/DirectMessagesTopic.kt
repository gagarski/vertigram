package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [DirectMessagesTopic](https://core.telegram.org/bots/api#directmessagestopic) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class DirectMessagesTopic internal constructor(
    val topicId: Long,
    val user: User? = null
) {
    companion object
}
