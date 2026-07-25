package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Represents a video file.
 *
 * See Telegram's [Video](https://core.telegram.org/bots/api#video) documentation.
 */
@TelegramCodegen.Type
data class Video internal constructor(
    /** Identifier for downloading or reusing this file. */
    val fileId: String,
    /** Unique identifier for this file. */
    val fileUniqueId: String,
    /** Video width. */
    val width: Int,
    /** Video height. */
    val height: Int,
    /** Duration of the video. */
    val duration: Duration,
    /** Video thumbnail. */
    val thumbnail: PhotoSize? = null,
    /** Available sizes of the cover of the video. */
    val cover: List<PhotoSize>? = null,
    /** Start timestamp for the video in the message. */
    val startTimestamp: Duration? = null,
    /** Available qualities of the video. */
    val qualities: List<Quality>? = null,
    /** Original filename. */
    val fileName: String? = null,
    /** MIME type of the file. */
    val mimeType: String? = null,
    /** File size in bytes. */
    val fileSize: Long? = null
) {
    /**
     * Describes one quality of a video.
     *
     * See Telegram's [VideoQuality](https://core.telegram.org/bots/api#videoquality) documentation.
     */
    @TelegramCodegen.Type
    data class Quality internal constructor(
        /** Identifier for downloading or reusing this file. */
        val fileId: String,
        /** Unique identifier for this file. */
        val fileUniqueId: String,
        /** Video width. */
        val width: Int,
        /** Video height. */
        val height: Int,
        /** Codec used for the video. */
        val codec: String,
        /** File size in bytes. */
        val fileSize: Long? = null
    ) {
        companion object
    }

    companion object
}
