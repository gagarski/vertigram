package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Describes a topic of a direct messages chat.
 *
 * See Telegram's [DirectMessagesTopic](https://core.telegram.org/bots/api#directmessagestopic) documentation.
 */
@TelegramCodegen.Type
data class DirectMessagesTopic internal constructor(
    /** Unique identifier of the topic. */
    val topicId: Long,
    /** Information about the user that created the topic. Currently, it is always present. */
    val user: User? = null
) {
    companion object
}
