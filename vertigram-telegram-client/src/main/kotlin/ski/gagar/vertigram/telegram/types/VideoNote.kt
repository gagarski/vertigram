package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Represents a video message.
 *
 * See Telegram's [VideoNote](https://core.telegram.org/bots/api#videonote) documentation.
 */
@TelegramCodegen.Type
data class VideoNote internal constructor(
    /** Identifier for downloading or reusing this file. */
    val fileId: String,
    /** Unique identifier for this file. */
    val fileUniqueId: String,
    /** Video width and height. */
    val length: Int,
    /** Duration of the video. */
    val duration: Duration,
    /** Video thumbnail. */
    val thumbnail: PhotoSize? = null,
    /** File size in bytes. */
    val fileSize: Long? = null
) {
    companion object
}
