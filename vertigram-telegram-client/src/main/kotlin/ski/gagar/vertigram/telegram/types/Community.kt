package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Represents a community (a group of chats).
 *
 * See Telegram's [Community](https://core.telegram.org/bots/api#community) documentation.
 */
@TelegramCodegen.Type
data class Community internal constructor(
    /**
     * Unique identifier for this community.
     */
    val id: Long,
    /** Name of the community. */
    val name: String
) {
    companion object
}
