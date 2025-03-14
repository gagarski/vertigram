package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [editForumTopic](https://core.telegram.org/bots/api#editforumtopic) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen
data class EditForumTopic(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long,
    val name: String? = null,
    val iconCustomEmojiId: String? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
