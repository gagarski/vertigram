package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [deleteStory](https://core.telegram.org/bots/api#deletestory) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class DeleteStory internal constructor(
    val businessConnectionId: String,
    val storyId: Long
) : JsonTelegramCallable<Boolean>() {
    companion object
}