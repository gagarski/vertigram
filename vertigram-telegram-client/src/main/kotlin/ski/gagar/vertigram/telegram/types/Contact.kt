package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [Contact](https://core.telegram.org/bots/api#contact) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Contact internal constructor(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null
) {
    companion object
}
