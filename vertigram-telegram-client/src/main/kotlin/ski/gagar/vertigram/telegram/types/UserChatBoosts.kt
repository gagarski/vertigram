package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Contains a list of boosts added to a chat by a user.
 *
 * See Telegram's [UserChatBoosts](https://core.telegram.org/bots/api#userchatboosts) documentation.
 */
@TelegramCodegen.Type
data class UserChatBoosts internal constructor(
    /** Boosts added to the chat by the user. */
    val boosts: List<ChatBoost>
) {
    companion object
}
