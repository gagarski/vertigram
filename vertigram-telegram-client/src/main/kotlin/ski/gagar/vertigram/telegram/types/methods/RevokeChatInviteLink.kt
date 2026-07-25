package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ChatInviteLink
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to revoke an invite link created by the bot.
 *
 * The bot must be an administrator in the chat for this to work and must have appropriate administrator rights.
 * Returns the revoked invite link.
 *
 * See Telegram's [revokeChatInviteLink](https://core.telegram.org/bots/api#revokechatinvitelink) documentation.
 */
@TelegramCodegen.Method
data class RevokeChatInviteLink internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Invite link to revoke. */
    val inviteLink: String
) : JsonTelegramCallable<ChatInviteLink>(), HasChatId
