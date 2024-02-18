package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

data class Sticker(
    val fileId: String,
    val fileUniqueId: String,
    val type: StickerType,
    val width: Int,
    val height: Int,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean = false,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean = false,
    val thumbnail: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val premiumAnimation: File? = null,
    val maskPosition: MaskPosition? = null,
    val customEmojiId: String? = null,
    val fileSize: Long? = null,
    // Since Bot API 6.6
    val needsRepainting: Boolean = false
) {
    /**
     * Telegram [MaskPosition](https://core.telegram.org/bots/api#maskposition) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class MaskPosition(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val point: String,
        val xShift: Double,
        val yShift: Double,
        val scale: Double
    )

}
