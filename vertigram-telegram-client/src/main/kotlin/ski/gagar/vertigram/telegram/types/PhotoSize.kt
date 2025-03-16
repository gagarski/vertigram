package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [PhotoSize](https://core.telegram.org/bots/api#photosize) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class PhotoSize internal constructor(
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val fileSize: Long? = null
) {
    companion object
}
