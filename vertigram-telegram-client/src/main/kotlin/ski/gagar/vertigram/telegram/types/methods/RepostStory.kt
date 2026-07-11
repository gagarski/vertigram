package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Story

/**
 * Telegram [repostStory](https://core.telegram.org/bots/api#repoststory) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class RepostStory internal constructor(
    val businessConnectionId: String,
    val fromChatId: Long,
    val fromStoryId: Long,
    val activePeriod: PostStory.ActivePeriod,
    val postToChatPage: Boolean = false,
    val protectContent: Boolean = false
) : JsonTelegramCallable<Story>()
