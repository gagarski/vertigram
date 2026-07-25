package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.InputChecklist
import ski.gagar.vertigram.telegram.types.ReplyMarkup

/**
 * Use this method to edit a checklist on behalf of a connected business account.
 *
 * See Telegram's [editMessageChecklist](https://core.telegram.org/bots/api#editmessagechecklist) documentation.
 */
@TelegramCodegen.Method()
data class EditMessageChecklist internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String,
    /** Unique identifier for the target chat. */
    override val chatId: Long,
    /** Unique identifier of the message containing the checklist. */
    val messageId: Long,
    /** New content of the checklist. */
    val checklist: InputChecklist,
    /** New inline keyboard for the message. */
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
