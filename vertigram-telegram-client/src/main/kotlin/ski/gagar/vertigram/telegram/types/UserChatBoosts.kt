package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [UserChatBoosts](https://core.telegram.org/bots/api#userchatboosts) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class UserChatBoosts internal constructor(
    val boosts: List<ChatBoost>
) {
    companion object
}
