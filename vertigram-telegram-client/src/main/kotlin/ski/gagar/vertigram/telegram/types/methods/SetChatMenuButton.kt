package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.MenuButton
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to change the bot's menu button in a private chat, or the default menu button.
 *
 * Returns `true` on success.
 *
 * See Telegram's [setChatMenuButton](https://core.telegram.org/bots/api#setchatmenubutton) documentation.
 */
@TelegramCodegen.Method
data class SetChatMenuButton internal constructor(
    /** Unique identifier for the target private chat. */
    override val chatId: Long? = null,
    /** New menu button for the private chat or the new default menu button. */
    val menuButton: MenuButton? = null
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
