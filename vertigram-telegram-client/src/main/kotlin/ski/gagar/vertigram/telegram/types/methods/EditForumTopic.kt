package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to edit name and icon of a topic in a forum supergroup chat.
 *
 * The bot must be an administrator in the chat for this to work and must have
 * [ski.gagar.vertigram.telegram.types.ChatAdministratorRights.canManageTopics] administrator rights, unless it is
 * the creator of the topic. Returns `true` on success.
 *
 * See Telegram's [editForumTopic](https://core.telegram.org/bots/api#editforumtopic) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class EditForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread of the forum topic. */
    val messageThreadId: Long,
    /** New topic name, 0-128 characters. */
    val name: String? = null,
    /** New unique identifier of the custom emoji shown as the topic icon. */
    val iconCustomEmojiId: String? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
