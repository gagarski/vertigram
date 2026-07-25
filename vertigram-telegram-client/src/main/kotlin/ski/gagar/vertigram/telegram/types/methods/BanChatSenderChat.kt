package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to ban a channel chat in a supergroup or a channel. Until the chat is
 * [unbanned][ski.gagar.vertigram.telegram.methods.unbanChatSenderChat], the owner of the banned chat won't be able to
 * send messages on behalf of any of their channels. The bot must be an administrator in the supergroup or channel for
 * this to work and must have the appropriate administrator rights. Returns `true` on success.
 *
 * See Telegram's [banChatSenderChat](https://core.telegram.org/bots/api#banchatsenderchat) documentation.
 */
@TelegramCodegen.Method
data class BanChatSenderChat internal constructor(
    /** Unique identifier for the target chat or username of the target channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target sender chat. */
    val senderChatId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
