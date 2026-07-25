package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Duration

/**
 * This object represents a live photo.
 *
 * See Telegram's [LivePhoto](https://core.telegram.org/bots/api#livephoto) documentation.
 */
@TelegramCodegen.Type
data class LivePhoto internal constructor(
    /** Available sizes of the corresponding static photo. */
    val photo: List<PhotoSize>? = null,
    /** Identifier for the video file, which can be used to download or reuse the file. */
    val fileId: String,
    /**
     * Unique identifier for the video file, which is supposed to be the same over time and for different bots. Can't
     * be used to download or reuse the file.
     */
    val fileUniqueId: String,
    /** Video width as defined by the sender. */
    val width: Int,
    /** Video height as defined by the sender. */
    val height: Int,
    /** Duration of the video as defined by the sender. */
    val duration: Duration,
    /** MIME type of the file as defined by the sender. */
    val mimeType: String? = null,
    /** File size in bytes. */
    val fileSize: Long? = null
) {
    companion object
}
