package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [setChatPhoto](https://core.telegram.org/bots/api#setchatphoto) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@Throttled
@TelegramCodegen.Method
data class SetChatPhoto internal constructor(
    override val chatId: ChatId,
    val photo: Attachment
) : MultipartTelegramCallable<Boolean>(), HasChatId
