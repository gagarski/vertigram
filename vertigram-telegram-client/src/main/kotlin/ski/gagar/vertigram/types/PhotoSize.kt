package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [PhotoSize](https://core.telegram.org/bots/api#photosize) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class PhotoSize(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val fileId: String,
    val fileUniqueId: String,
    val width: Int,
    val height: Int,
    val fileSize: Long? = null
)
