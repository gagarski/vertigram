package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to delete multiple messages simultaneously. If some of the specified messages can't be found, they
 * are skipped. Returns `true` on success.
 *
 * See Telegram's [deleteMessages](https://core.telegram.org/bots/api#deletemessages) documentation.
 */
@TelegramCodegen.Method
data class DeleteMessages internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /**
     * List of 1-100 identifiers of messages to delete. See
     * [ski.gagar.vertigram.telegram.methods.deleteMessage] for limitations on which messages can be deleted.
     */
    val messageIds: List<Long>
) : JsonTelegramCallable<Boolean>(), HasChatId
