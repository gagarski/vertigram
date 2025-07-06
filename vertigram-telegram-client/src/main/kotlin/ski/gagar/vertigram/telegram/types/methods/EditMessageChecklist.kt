package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.InputChecklist
import ski.gagar.vertigram.telegram.types.ReplyMarkup

/**
 * Telegram [editMessageChecklist](https://core.telegram.org/bots/api#editmessagechecklist) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class EditMessageChecklist internal constructor(
    val businessConnectionId: String,
    override val chatId: Long,
    val messageId: Long,
    val checklist: InputChecklist,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
