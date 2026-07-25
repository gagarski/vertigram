package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to unban a previously banned channel chat in a supergroup or channel. Returns `true` on success.
 *
 * See Telegram's [unbanChatSenderChat](https://core.telegram.org/bots/api#unbanchatsenderchat) documentation.
 */
@TelegramCodegen.Method
data class UnbanChatSenderChat internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target sender chat. */
    val senderChatId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
