package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [EncryptedCredentials](https://core.telegram.org/bots/api#encryptedcredentials) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class EncryptedCredentials(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val data: String,
    val hash: String,
    val secret: String
)
