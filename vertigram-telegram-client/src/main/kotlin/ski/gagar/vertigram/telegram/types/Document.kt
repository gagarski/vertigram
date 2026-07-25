package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents a general file, as opposed to photos, voice messages and audio files.
 *
 * See Telegram's [Document](https://core.telegram.org/bots/api#document) documentation.
 */
@TelegramCodegen.Type
data class Document internal constructor(
    /** Identifier for this file, which can be used to download or reuse the file. */
    val fileId: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     * used to download or reuse the file.
     */
    val fileUniqueId: String,
    /** Document thumbnail as defined by the sender. */
    val thumbnail: PhotoSize? = null,
    /** Original filename as defined by the sender. */
    val fileName: String? = null,
    /** MIME type of the file as defined by the sender. */
    val mimeType: String? = null,
    /** File size in bytes. */
    val fileSize: Long? = null
) {
    companion object
}
