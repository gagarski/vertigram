package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [CallbackQuery](https://core.telegram.org/bots/api#callbackquery) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class CallbackQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val id: String,
    val from: User,
    val message: Message? = null,
    val inlineMessageId: String? = null,
    val chatInstance: String,
    val data: String? = null,
    val gameShortName: String? = null
)
