package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [BusinessIntro](https://core.telegram.org/bots/api#businessintro) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BusinessIntro internal constructor(
    val title: String? = null,
    val message: String? = null,
    val sticker: Sticker? = null
) {
    companion object
}