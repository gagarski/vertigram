package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents a file ready to be downloaded. The file can be downloaded via
 * `https://api.telegram.org/file/bot<token>/<file_path>`. The link is valid for at least 1 hour; when it expires, a new
 * one can be requested with [getFile][ski.gagar.vertigram.telegram.methods.getFile]. The maximum file size to download
 * is 20 MB.
 *
 * See Telegram's [File](https://core.telegram.org/bots/api#file) documentation.
 */
@TelegramCodegen.Type
data class File internal constructor(
    /** Identifier for this file, which can be used to download or reuse the file. */
    val fileId: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     * used to download or reuse the file.
     */
    val fileUniqueId: String,
    /** File size in bytes. */
    val fileSize: Long? = null,
    /** File path used to download the file. */
    val filePath: String? = null
) {
    companion object
}
