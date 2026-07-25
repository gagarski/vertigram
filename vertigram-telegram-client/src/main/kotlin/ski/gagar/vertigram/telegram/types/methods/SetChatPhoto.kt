package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to set a new profile photo for the chat. Returns `true` on success.
 *
 * See Telegram's [setChatPhoto](https://core.telegram.org/bots/api#setchatphoto) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SetChatPhoto internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** New chat photo. */
    val photo: Attachment
) : MultipartTelegramCallable<Boolean>(), HasChatId
