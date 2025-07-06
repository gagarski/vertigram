package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.InputChecklist
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters

/**
 * Telegram [sendChecklist](https://core.telegram.org/bots/api#sendchecklist) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class SendChecklist internal constructor(
    val businessConnectionId: String,
    override val chatId: Long,
    val checklist: InputChecklist,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val messageEffectId: String? = null,
    val replyParameters: ReplyParameters? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
