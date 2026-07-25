package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.ChatAdministratorRights
import ski.gagar.vertigram.telegram.types.ForumTopic
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Use this method to create a topic in a forum supergroup chat or a private chat with a user. In the case of a
 * supergroup chat, the bot must be an administrator in the chat for this to work and must have the
 * [ChatAdministratorRights.canManageTopics] administrator rights. Returns information about the created topic as a
 * [ForumTopic].
 *
 * See Telegram's [createForumTopic](https://core.telegram.org/bots/api#createforumtopic) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class CreateForumTopic internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup. */
    override val chatId: ChatId,
    /** Topic name, 1-128 characters. */
    val name: String,
    /** Color of the topic icon. */
    val iconColor: ForumTopic.Color? = null,
    /**
     * Unique identifier of the custom emoji shown as the topic icon. Use
     * [ski.gagar.vertigram.telegram.methods.getForumTopicIconStickers] to get all allowed custom emoji identifiers.
     */
    val iconCustomEmojiId: String? = null
) : JsonTelegramCallable<ForumTopic>(), HasChatId
