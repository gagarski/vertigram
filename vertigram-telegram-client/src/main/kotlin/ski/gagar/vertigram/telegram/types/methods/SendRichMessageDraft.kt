package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputRichMessage

/**
 * Use this method to stream a partial rich message to a user while the message is being generated.
 *
 * Returns `true` on success.
 *
 * See Telegram's [sendRichMessageDraft](https://core.telegram.org/bots/api#sendrichmessagedraft) documentation.
 */
@TelegramCodegen.Method
data class SendRichMessageDraft internal constructor(
    /** Unique identifier for the target private chat. */
    val chatId: Long,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Non-zero unique identifier of the message draft. */
    val draftId: Long,
    /** Partial rich message to stream. */
    val richMessage: InputRichMessage
) : MultipartTelegramCallable<Boolean>()
