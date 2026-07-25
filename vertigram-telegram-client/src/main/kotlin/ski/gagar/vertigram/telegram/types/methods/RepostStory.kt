package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Story

/**
 * Reposts a story on behalf of a business account from another business account.
 *
 * Both business accounts must be managed by the same bot, and the source story must have been posted or reposted by
 * the bot. Requires the [ski.gagar.vertigram.telegram.types.BusinessConnection.BotRights.canManageStories] business
 * bot right for both accounts.
 *
 * See Telegram's [repostStory](https://core.telegram.org/bots/api#repoststory) documentation.
 */
@TelegramCodegen.Method
data class RepostStory internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Unique identifier of the chat which posted the story to repost. */
    val fromChatId: Long,
    /** Unique identifier of the story to repost. */
    val fromStoryId: Long,
    /** Period after which the story is moved to the archive. */
    val activePeriod: PostStory.ActivePeriod,
    /** Pass `true` to keep the story accessible after it expires. */
    val postToChatPage: Boolean = false,
    /** Pass `true` if the story must be protected from forwarding and screenshotting. */
    val protectContent: Boolean = false
) : JsonTelegramCallable<Story>()
