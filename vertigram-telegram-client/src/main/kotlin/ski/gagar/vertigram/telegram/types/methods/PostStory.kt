package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonValue
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.client.Telegram
import ski.gagar.vertigram.telegram.types.InputMedia
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.Story
import ski.gagar.vertigram.telegram.types.StoryArea
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [postStory](https://core.telegram.org/bots/api#poststory) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
//@TelegramCodegen.Method()
data class PostStory internal constructor(
    val businessConnectionId: String,
    @TelegramMedia
    val content: InputMedia.StoryContent,
    val activePeriod: ActivePeriod,
    val caption: String? = null,
    val parseMode: RichText.ParseMode? = null,
    val captionEntities: List<MessageEntity>? = null,
    val areas: List<StoryArea>? = null,
    val postToChatPage: Boolean = false,
    val protectContent: Boolean = false,
) : JsonTelegramCallable<Story>() {

    /**
     * A value for [PostStory.activePeriod] field
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

@Suppress("DEPRECATION")
public suspend fun Telegram.postStory(
    noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    chatId: ChatId,
    giftId: String,
    payForUpgrade: Boolean = false,
    richText: RichText? = null
): Boolean = call(
    SendGift.Chat(
        chatId = chatId,
        giftId = giftId,
        payForUpgrade = payForUpgrade,
        text = richText?.text,
        textParseMode = richText?.parseMode,
        textEntities = richText?.entities
    )
)