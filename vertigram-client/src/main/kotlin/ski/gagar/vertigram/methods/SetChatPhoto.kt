package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramMedia
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setChatPhoto](https://core.telegram.org/bots/api#setchatphoto) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SetChatPhoto(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    @TelegramMedia
    val photo: Attachment
) : MultipartTelegramCallable<Boolean>(), HasChatId
