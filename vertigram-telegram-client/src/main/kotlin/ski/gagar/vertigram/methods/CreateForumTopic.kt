package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.ForumTopic
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
