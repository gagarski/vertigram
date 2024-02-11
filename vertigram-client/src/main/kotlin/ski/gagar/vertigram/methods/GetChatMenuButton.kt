package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.throttling.HasChatIdLong
import ski.gagar.vertigram.types.MenuButton
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getChatMenuButton](https://core.telegram.org/bots/api#getchatmenubutton) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetChatMenuButton(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: Long? = null
) : JsonTelegramCallable<MenuButton>(), HasChatIdLong
