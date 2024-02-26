package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [StickerSet](https://core.telegram.org/bots/api#stickerset) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class StickerSet(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val name: String,
    val title: String,
    val stickerType: Sticker.Type,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean = false,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean = false,
    val stickers: List<Sticker> = listOf(),
    val thumbnail: List<PhotoSize>? = null
)
