package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ChatPermissions
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

/**
 * Use this method to restrict a user in a supergroup.
 *
 * The bot must be an administrator in the supergroup for this to work and must have appropriate administrator
 * rights. Pass `true` for all permissions to lift restrictions from a user. Returns `true` on success.
 *
 * See Telegram's [restrictChatMember](https://core.telegram.org/bots/api#restrictchatmember) documentation.
 */
@TelegramCodegen.Method
data class RestrictChatMember internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier of the target user. */
    val userId: Long,
    /** New user permissions. */
    val permissions: ChatPermissions,
    /** Pass `true` to use independent chat permissions. */
    val useIndependentChatPermissions: Boolean = false,
    /** Date when restrictions will be lifted for the user. */
    val untilDate: Instant? = null
) : JsonTelegramCallable<Boolean>(), HasChatId
