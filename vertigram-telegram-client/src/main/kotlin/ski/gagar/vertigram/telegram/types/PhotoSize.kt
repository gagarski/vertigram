package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents one size of a photo or a file/sticker thumbnail.
 *
 * See Telegram's [PhotoSize](https://core.telegram.org/bots/api#photosize) documentation.
 */
@TelegramCodegen.Type
data class PhotoSize internal constructor(
    /** Identifier for downloading or reusing this file. */
    val fileId: String,
    /** Unique identifier for this file. */
    val fileUniqueId: String,
    /** Photo width. */
    val width: Int,
    /** Photo height. */
    val height: Int,
    /** File size in bytes. */
    val fileSize: Long? = null
) {
    companion object
}
