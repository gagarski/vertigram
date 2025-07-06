package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.Story
import ski.gagar.vertigram.telegram.types.StoryArea
import ski.gagar.vertigram.telegram.types.richtext.RichText

/**
 * Telegram [editStory](https://core.telegram.org/bots/api#editstory) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class EditStory internal constructor(
    val businessConnectionId: String,
    val storyId: Long,
    @TelegramMedia
    val content: InputMedia.StoryContent,
    val caption: String? = null,
    val parseMode: RichText.ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val areas: List<StoryArea>? = null
) : JsonTelegramCallable<Story>() {
    companion object
}