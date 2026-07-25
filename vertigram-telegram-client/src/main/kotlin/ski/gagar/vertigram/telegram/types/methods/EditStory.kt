package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.Story
import ski.gagar.vertigram.telegram.types.StoryArea
import ski.gagar.vertigram.telegram.types.richtext.RichText

/**
 * Edits a story previously posted by the bot on behalf of a managed business account.
 *
 * Requires the [ski.gagar.vertigram.telegram.types.BusinessConnection.BotRights.canManageStories] business bot right.
 *
 * See Telegram's [editStory](https://core.telegram.org/bots/api#editstory) documentation.
 */
@TelegramCodegen.Method()
data class EditStory internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Unique identifier of the story to edit. */
    val storyId: Long,
    /** Content of the story. */
    val content: InputMedia.StoryContent,
    /** Caption of the story, 0-2048 characters after entities parsing. */
    val caption: String? = null,
    /** Mode for parsing entities in the story caption. */
    val parseMode: RichText.ParseMode? = null,
    /** Special entities that appear in the caption; can be specified instead of [parseMode]. */
    val captionEntities: List<MessageEntity>? = null,
    /** Clickable areas to be shown on the story. */
    val areas: List<StoryArea>? = null
) : JsonTelegramCallable<Story>() {
    companion object
}
