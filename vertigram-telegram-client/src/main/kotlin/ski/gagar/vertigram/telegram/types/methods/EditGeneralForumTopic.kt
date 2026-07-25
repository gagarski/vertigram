package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit the name of the 'General' topic in a forum supergroup chat.
 *
 * The bot must be an administrator in the chat for this to work and must have
 * [ski.gagar.vertigram.telegram.types.ChatAdministratorRights.canManageTopics] administrator rights. Returns `true`
 * on success.
 *
 * See Telegram's [editGeneralForumTopic](https://core.telegram.org/bots/api#editgeneralforumtopic) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class EditGeneralForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** New topic name, 1-128 characters. */
    val name: String
) : JsonTelegramCallable<Boolean>(), HasChatId
