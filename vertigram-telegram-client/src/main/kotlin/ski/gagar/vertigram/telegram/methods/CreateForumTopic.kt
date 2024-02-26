package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ForumTopic
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [createForumTopic](https://core.telegram.org/bots/api#createforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class CreateForumTopic(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val name: String,
    val iconColor: ForumTopic.Color? = null,
    val iconCustomEmojiId: String? = null
) : JsonTelegramCallable<ForumTopic>(), HasChatId
