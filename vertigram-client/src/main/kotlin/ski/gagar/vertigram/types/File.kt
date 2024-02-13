package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [File](https://core.telegram.org/bots/api#file) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class File(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val fileId: String,
    val fileUniqueId: String,
    val fileSize: Long? = null,
    val filePath: String? = null
)
