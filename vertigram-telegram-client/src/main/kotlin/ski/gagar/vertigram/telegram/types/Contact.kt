package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents a phone contact.
 *
 * See Telegram's [Contact](https://core.telegram.org/bots/api#contact) documentation.
 */
@TelegramCodegen.Type
data class Contact internal constructor(
    /** Contact's phone number. */
    val phoneNumber: String,
    /** Contact's first name. */
    val firstName: String,
    /** Contact's last name. */
    val lastName: String? = null,
    /**
     * Contact's user identifier in Telegram.
     */
    val userId: Long? = null,
    /** Additional data about the contact in the form of a [vCard](https://en.wikipedia.org/wiki/VCard). */
    val vcard: String? = null
) {
    companion object
}
