package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [LoginUrl](https://core.telegram.org/bots/api#loginurl) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class LoginUrl(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val url: String,
    val forwardText: String? = null,
    val botUsername: String? = null,
    val requestWriteAccess: Boolean = false
)
