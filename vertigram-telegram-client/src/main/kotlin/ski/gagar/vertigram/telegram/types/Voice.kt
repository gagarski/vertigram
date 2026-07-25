package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Represents a voice note.
 *
 * See Telegram's [Voice](https://core.telegram.org/bots/api#voice) documentation.
 */
@TelegramCodegen.Type
data class Voice internal constructor(
    /** Identifier for downloading or reusing this file. */
    val fileId: String,
    /** Unique identifier for this file. */
    val fileUniqueId: String,
    /** Duration of the audio. */
    val duration: Duration,
    /** MIME type of the file. */
    val mimeType: String? = null,
    /** File size in bytes. */
    val fileSize: Long? = null
) {
    companion object
}
