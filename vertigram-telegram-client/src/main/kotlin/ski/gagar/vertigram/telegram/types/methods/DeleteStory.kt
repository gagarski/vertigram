package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BusinessConnection

/**
 * Deletes a story previously posted by the bot on behalf of a managed business account. Requires the
 * [BusinessConnection.BotRights.canManageStories] business bot right. Returns `true` on success.
 *
 * See Telegram's [deleteStory](https://core.telegram.org/bots/api#deletestory) documentation.
 */
@TelegramCodegen.Method
data class DeleteStory internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Unique identifier of the story to delete. */
    val storyId: Long
) : JsonTelegramCallable<Boolean>() {
    companion object
}
