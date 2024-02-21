package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [VideoNote](https://core.telegram.org/bots/api#videonote) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class VideoNote(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val fileId: String,
    val fileUniqueId: String,
    val length: Int,
    val duration: Duration,
    val thumbnail: PhotoSize? = null,
    val fileSize: Long? = null
)
