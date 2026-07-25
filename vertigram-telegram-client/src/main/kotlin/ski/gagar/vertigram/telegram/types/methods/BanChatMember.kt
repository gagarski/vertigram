package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import java.time.Instant

/**
 * Use this method to ban a user in a group, a supergroup or a channel. In the case of supergroups and channels, the
 * user will not be able to return to the chat on their own using invite links, etc., unless
 * [ski.gagar.vertigram.telegram.methods.unbanChatMember] is called first. The bot must be an administrator in the chat
 * for this to work and must have the appropriate administrator rights. Returns `true` on success.
 *
 * See Telegram's [banChatMember](https://core.telegram.org/bots/api#banchatmember) documentation.
 */
@TelegramCodegen.Method
data class BanChatMember internal constructor(
    /** Unique identifier for the target chat or username of the target supergroup or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long,
    /**
     * Date when the user will be unbanned. If the user is banned for more than 366 days or less than 30 seconds from
     * the current time, they are considered to be banned forever. Applied for supergroups and channels only.
     */
    val untilDate: Instant? = null,
    /**
     * Pass `true` to delete all messages from the chat for the user that is being removed. If `false`, the user will be
     * able to see messages in the group that were sent before the user was removed. Always `true` for supergroups and
     * channels.
     */
    val revokeMessages: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
