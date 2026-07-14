package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputRichMessage

/**
 * Telegram [sendRichMessageDraft](https://core.telegram.org/bots/api#sendrichmessagedraft) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SendRichMessageDraft internal constructor(
    val chatId: Long,
    val messageThreadId: Long? = null,
    val draftId: Long,
    val richMessage: InputRichMessage
) : MultipartTelegramCallable<Boolean>()
