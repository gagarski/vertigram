package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * A type described in [Making requests](https://core.telegram.org/bots/api#making-requests) section,
 * describes a top-level entity of the response from the API
 */
data class Wrapper<T> internal constructor(
    val ok: Boolean,
    val result: T?,
    val description: String? = null,
    val parameters: ResponseParameters? = null
) {
    companion object
}