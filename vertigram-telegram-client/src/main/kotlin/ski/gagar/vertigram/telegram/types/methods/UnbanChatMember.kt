package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to unban a previously banned user in a supergroup or channel.
 *
 * Returns `true` on success.
 *
 * See Telegram's [unbanChatMember](https://core.telegram.org/bots/api#unbanchatmember) documentation.
 */
@TelegramCodegen.Method
data class UnbanChatMember internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long,
    /** Pass `true` to do nothing if the user is not banned. */
    val onlyIfBanned: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
