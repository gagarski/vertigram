package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.attachments.Attachment
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
    @ski.gagar.vertigram.telegram.annotations.TelegramMedia
    val photo: Attachment
) : MultipartTelegramCallable<Boolean>(), HasChatId
