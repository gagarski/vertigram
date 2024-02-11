package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatIdLong
import ski.gagar.vertigram.types.MenuButton
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [setChatMenuButton](https://core.telegram.org/bots/api#setchatmenubutton) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class SetChatMenuButton(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: Long? = null,
    val menuButton: MenuButton? = null
) : JsonTelegramCallable<Boolean>(), HasChatIdLong
