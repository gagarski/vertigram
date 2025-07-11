package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.MenuButton
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setChatMenuButton](https://core.telegram.org/bots/api#setchatmenubutton) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetChatMenuButton internal constructor(
    override val chatId: Long? = null,
    val menuButton: MenuButton? = null
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
