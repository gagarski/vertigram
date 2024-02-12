package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.types.ChatPermissions
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setChatPermissions](https://core.telegram.org/bots/api#setchatpermissions) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetChatPermissions(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val permissions: ChatPermissions,
    val useIndependentChatPermissions: Boolean = false
) : JsonTelegramCallable<Boolean>(), HasChatId
