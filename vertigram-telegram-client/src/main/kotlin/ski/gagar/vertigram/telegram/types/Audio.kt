package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * This object represents an audio file to be treated as music by the Telegram clients.
 *
 * See Telegram's [Audio](https://core.telegram.org/bots/api#audio) documentation.
 */
@TelegramCodegen.Type
data class Audio internal constructor(
    /** Identifier for this file, which can be used to download or reuse the file. */
    val fileId: String,
    /**
     * Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be
     * used to download or reuse the file.
     */
    val fileUniqueId: String,
    /** Duration of the audio as defined by the sender. */
    val duration: Duration,
    /** Performer of the audio as defined by the sender or by audio tags. */
    val performer: String? = null,
    /** Title of the audio as defined by the sender or by audio tags. */
    val title: String? = null,
    /** Original filename as defined by the sender. */
    val fileName: String? = null,
    /** MIME type of the file as defined by the sender. */
    val mimeType: String? = null,
    /** File size in bytes. */
    val fileSize: Long? = null,
    /** Thumbnail of the album cover to which the music file belongs. */
    val thumbnail: PhotoSize? = null
) {
    companion object
}
