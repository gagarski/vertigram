package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to set a custom title for an administrator in a supergroup promoted by the bot.
 *
 * Returns `true` on success.
 *
 * See Telegram's
 * [setChatAdministratorCustomTitle](https://core.telegram.org/bots/api#setchatadministratorcustomtitle)
 * documentation.
 */
@TelegramCodegen.Method
data class SetChatAdministratorCustomTitle internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long,
    /** New custom title for the administrator; 0-16 characters, emoji aren't allowed. */
    val customTitle: String
) : JsonTelegramCallable<Boolean>(), HasChatId
