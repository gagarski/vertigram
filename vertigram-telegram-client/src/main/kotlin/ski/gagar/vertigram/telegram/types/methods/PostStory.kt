package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonValue
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.Story
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import java.time.Duration

/**
 * Posts a story on behalf of a managed business account.
 *
 * Requires the [ski.gagar.vertigram.telegram.types.BusinessConnection.BotRights.canManageStories] business bot right.
 *
 * See Telegram's [postStory](https://core.telegram.org/bots/api#poststory) documentation.
 */
@TelegramCodegen.Method()
data class PostStory internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Content of the story. */
    val content: InputMedia.StoryContent,
    /** Period after which the story is moved to the archive. */
    val activePeriod: ActivePeriod,
    /** Caption of the story, 0-2048 characters after entities parsing. */
    val caption: String? = null,
    /** Mode for parsing entities in the story caption. */
    val parseMode: FormattedText.ParseMode? = null,
    /** Special entities that appear in the caption; can be specified instead of [parseMode]. */
    val captionEntities: List<MessageEntity>? = null,
    /** Clickable areas to be shown on the story. */
    val areas: List<Story.Area>? = null,
    /** Pass `true` to keep the story accessible after it expires. */
    val postToChatPage: Boolean = false,
    /** Pass `true` if the story must be protected from forwarding and screenshotting. */
    val protectContent: Boolean = false,
) : JsonTelegramCallable<Story>() {

    /**
     * A value for [PostStory.activePeriod].
     */
    enum class ActivePeriod(val duration: Duration) {
        SIX_HOURS(Duration.ofHours(6)),
        TWELVE_HOURS(Duration.ofHours(12)),
        ONE_DAY(Duration.ofDays(1)),
        TWO_DAYS(Duration.ofDays(2));

        @JsonValue
        fun durationSec() = duration.toSeconds()
    }
    companion object
}
