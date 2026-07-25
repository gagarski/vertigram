package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.MenuButton
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button.
 *
 * See Telegram's [getChatMenuButton](https://core.telegram.org/bots/api#getchatmenubutton) documentation.
 */
@TelegramCodegen.Method
data class GetChatMenuButton internal constructor(
    /** Unique identifier for the target private chat. If omitted, the bot's default menu button is returned. */
    override val chatId: Long? = null
) : JsonTelegramCallable<MenuButton>(), HasChatIdLong
