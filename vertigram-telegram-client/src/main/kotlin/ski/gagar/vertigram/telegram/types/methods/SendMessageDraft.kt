package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText

/**
 * Use this method to stream a partial message to a user while the message is being generated.
 *
 * Returns `true` on success.
 *
 * See Telegram's [sendMessageDraft](https://core.telegram.org/bots/api#sendmessagedraft) documentation.
 */
@TelegramCodegen.Method
data class SendMessageDraft internal constructor(
    /** Unique identifier for the target private chat. */
    val chatId: Long,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Non-zero unique identifier of the message draft. */
    val draftId: Long,
    /** Partial message text to stream. */
    val text: String? = null,
    /** Mode for parsing entities in [text]. */
    val parseMode: FormattedText.ParseMode? = null,
    /** Special entities that appear in [text]; can be specified instead of [parseMode]. */
    val entities: List<MessageEntity>? = null
) : JsonTelegramCallable<Boolean>()
