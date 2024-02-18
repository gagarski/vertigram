package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [LinkPreviewOptions](https://core.telegram.org/bots/api#linkpreviewoptions) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class LinkPreviewOptions(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    @get:JvmName("getIsDisabled")
    val isDisabled: Boolean = false,
    val url: String? = null,
    val preferSmallMedia: Boolean = false,
    val preferLargeMedia: Boolean = false,
    val showAboveText: Boolean = false
)
