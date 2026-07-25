package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.telegram.types.ChatPermissions
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to set default chat permissions for all members. Returns `true` on success.
 *
 * See Telegram's [setChatPermissions](https://core.telegram.org/bots/api#setchatpermissions) documentation.
 */
@TelegramCodegen.Method
data class SetChatPermissions internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** New default chat permissions. */
    val permissions: ChatPermissions,
    /** Pass `true` to set chat permissions independently. */
    val useIndependentChatPermissions: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
