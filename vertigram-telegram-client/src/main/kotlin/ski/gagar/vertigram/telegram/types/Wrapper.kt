package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Top-level response from the Telegram Bot API.
 *
 * See Telegram's [Making requests](https://core.telegram.org/bots/api#making-requests) documentation.
 */
data class Wrapper<T> internal constructor(
    /** Whether the request was successful. */
    val ok: Boolean,
    /** Result of a successful request. */
    val result: T?,
    /** Human-readable description of the result or error. */
    val description: String? = null,
    /** Additional information that can help handle an error. */
    val parameters: ResponseParameters? = null
) {
    companion object
}
