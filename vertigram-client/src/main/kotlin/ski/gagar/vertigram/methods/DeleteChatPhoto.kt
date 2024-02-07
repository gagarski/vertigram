package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [deleteChatPhoto](https://core.telegram.org/bots/api#deletechatphoto) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TgMethod
@Throttled
data class DeleteChatPhoto(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId
) : JsonTgCallable<Boolean>(), HasChatId
