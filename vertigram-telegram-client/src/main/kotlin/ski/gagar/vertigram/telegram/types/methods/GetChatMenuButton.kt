package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatIdLong
import ski.gagar.vertigram.telegram.types.MenuButton
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getChatMenuButton](https://core.telegram.org/bots/api#getchatmenubutton) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
data class GetChatMenuButton(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: Long? = null
) : JsonTelegramCallable<MenuButton>(), HasChatIdLong
