package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.richtext.RichText

/**
 * Telegram [sendMessageDraft](https://core.telegram.org/bots/api#sendmessagedraft) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SendMessageDraft internal constructor(
    val chatId: Long,
    val messageThreadId: Long? = null,
    val draftId: Long,
    val text: String? = null,
    val parseMode: RichText.ParseMode? = null,
    val entities: List<MessageEntity>? = null
) : JsonTelegramCallable<Boolean>()
