package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to delete a message, including service messages, with the following limitations:
 *
 * - A message can only be deleted if it was sent less than 48 hours ago.
 * - Service messages about a supergroup, channel, or forum topic creation can't be deleted.
 * - A dice message in a private chat can only be deleted if it was sent more than 24 hours ago.
 * - Bots can delete outgoing messages in private chats, groups, and supergroups.
 * - Bots can delete incoming messages in private chats.
 * - Bots granted [ChatAdministratorRights.canPostMessages] can delete outgoing messages in channels.
 * - If the bot is an administrator of a group, it can delete any message there.
 * - If the bot has the [ChatAdministratorRights.canDeleteMessages] administrator right in a supergroup or a channel,
 *   it can delete any message there.
 * - If the bot has the [ChatAdministratorRights.canManageDirectMessages] administrator right in a channel, it can
 *   delete any message in the corresponding direct messages chat.
 *
 * Returns `true` on success.
 *
 * See Telegram's [deleteMessage](https://core.telegram.org/bots/api#deletemessage) documentation.
 */
@TelegramCodegen.Method
data class DeleteMessage internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Identifier of the message to delete. */
    val messageId: Long
) : JsonTelegramCallable<Boolean>(), HasChatId
