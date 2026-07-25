package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to generate a new primary invite link for a chat; any previously generated primary link is revoked.
 *
 * The bot must be an administrator in the chat for this to work and must have appropriate administrator rights.
 * Returns the new invite link on success.
 *
 * See Telegram's [exportChatInviteLink](https://core.telegram.org/bots/api#exportchatinvitelink) documentation.
 */
@TelegramCodegen.Method
data class ExportChatInviteLink internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId
) : JsonTelegramCallable<String>(), HasChatId
